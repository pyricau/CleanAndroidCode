package info.piwai.cleanandroidcode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
@SupportedAnnotationTypes({
/*
 * Only supports @EActivity and @EFragment, since this is just a proof of
 * concept. This can be extended quite easily though.
 */
//
		"com.googlecode.androidannotations.annotations.EActivity", //
		"com.googlecode.androidannotations.annotations.EFragment", //
})
public class DaggerAAIntegrationProcessor extends AbstractProcessor {

	private static final String CACHE_PACKAGE_NAME = "";
	private static final String CACHE_FILE_NAME = "DaggerAAIntegrationCache.txt";

	/**
	 * We us this to avoid opening files twice in different rounds of the same
	 * processing, because it otherwhise fails. We shouldn't do this, but rather
	 * collect instructions on what we should do and output the module in the
	 * last round.
	 */
	boolean alreadyProcessed = false;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		Messager messager = processingEnv.getMessager();
		messager.printMessage(Diagnostic.Kind.NOTE, "Init of DaggerAAIntegrationProcessor");
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

		if (!alreadyProcessed) {
			Set<TypeElement> annotatedElements = getAnnotatedElements(roundEnv);

			generateDaggerModule(annotatedElements);

			cacheAnnotatedElements(annotatedElements);
			alreadyProcessed = true;
		}

		return false;
	}

	private Set<TypeElement> getAnnotatedElements(RoundEnvironment roundEnv) {
		Set<TypeElement> annotationTypeElements = getAnnotationTypeElements();

		Set<TypeElement> roundAnnotatedElements = getRoundAnnotatedElements(roundEnv, annotationTypeElements);

		Set<TypeElement> cachedElements = loadCachedAnnotatedElement(roundAnnotatedElements);

		Set<TypeElement> cachedAnnotatedElements = filterAnnotatedElements(cachedElements, annotationTypeElements);

		Set<TypeElement> annotatedElements = new HashSet<TypeElement>();
		annotatedElements.addAll(roundAnnotatedElements);
		annotatedElements.addAll(cachedAnnotatedElements);
		return annotatedElements;
	}

	private Set<TypeElement> filterAnnotatedElements(Set<TypeElement> typeElements, Set<TypeElement> annotationTypeElements) {
		Set<TypeElement> annotatedTypeElements = new HashSet<TypeElement>();

		for (TypeElement typeElement : typeElements) {
			List<? extends AnnotationMirror> annotationMirrors = typeElement.getAnnotationMirrors();
			boolean typeElementIsAnnotated = false;
			for (AnnotationMirror annotationMirror : annotationMirrors) {
				Element annotationElement = annotationMirror.getAnnotationType().asElement();
				if (annotationElement instanceof TypeElement) {
					TypeElement annotationTypeElement = (TypeElement) annotationElement;
					if (annotationTypeElements.contains(annotationTypeElement)) {
						typeElementIsAnnotated = true;
						break;
					}
				} else {
					reportNote(typeElement + " is annotated with an annotation element " + annotationElement + " that is not a TypeElement");
				}
			}
			if (typeElementIsAnnotated) {
				annotatedTypeElements.add(typeElement);
			} else {
				reportNote("Cached element " + typeElement + " is not annotated any more");
			}
		}

		return annotatedTypeElements;
	}

	private Set<TypeElement> getAnnotationTypeElements() {
		Set<String> annotationQualifiedNames = getSupportedAnnotationTypes();
		Set<TypeElement> annotationTypeElements = new HashSet<TypeElement>();
		Elements elementUtils = processingEnv.getElementUtils();
		for (String annotationQualifiedName : annotationQualifiedNames) {
			TypeElement annotationTypeElement = elementUtils.getTypeElement(annotationQualifiedName);
			if (annotationTypeElement != null) {
				annotationTypeElements.add(annotationTypeElement);
			} else {
				reportError("Could not find annotation type element for " + annotationQualifiedName);
			}
		}
		return annotationTypeElements;
	}

	private Set<TypeElement> getRoundAnnotatedElements(RoundEnvironment roundEnv, Set<TypeElement> annotationTypeElements) {
		Set<TypeElement> annotatedElements = new HashSet<TypeElement>();
		for (TypeElement annotationTypeElement : annotationTypeElements) {
			Set<? extends Element> elementsAnnotatedWithAnnotation = roundEnv.getElementsAnnotatedWith(annotationTypeElement);
			Set<TypeElement> typesAnnotatedWithAnnotation = ElementFilter.typesIn(elementsAnnotatedWithAnnotation);
			annotatedElements.addAll(typesAnnotatedWithAnnotation);
		}
		return annotatedElements;
	}

	/**
	 * 
	 * @param roundAnnotatedElements
	 *            the annotated elements found in the current round will not be
	 *            loaded
	 */
	private Set<TypeElement> loadCachedAnnotatedElement(Set<TypeElement> roundAnnotatedElements) {
		JavaFileManager.Location location = StandardLocation.SOURCE_OUTPUT;
		Filer filer = processingEnv.getFiler();
		try {
			FileObject resource = filer.getResource(location, CACHE_PACKAGE_NAME, CACHE_FILE_NAME);
			final boolean IGNORE_ENCODING_ERRORS = true;
			BufferedReader reader = new BufferedReader(resource.openReader(IGNORE_ENCODING_ERRORS));
			Elements elementUtils = processingEnv.getElementUtils();
			HashSet<TypeElement> cachedAnnotatedTypeElements = new HashSet<TypeElement>();
			String annotatedElementQualifiedName;
			while ((annotatedElementQualifiedName = reader.readLine()) != null) {
				if (annotatedElementQualifiedName.startsWith("#")) {
					continue;
				}
				TypeElement annotatedTypeElement = elementUtils.getTypeElement(annotatedElementQualifiedName);
				if (annotatedTypeElement != null) {
					if (!roundAnnotatedElements.contains(annotatedTypeElement)) {
						cachedAnnotatedTypeElements.add(annotatedTypeElement);
					}
				} else {
					reportNote("Could not find cached annotated type element for " + annotatedTypeElement);
				}
			}
			reader.close();
			return cachedAnnotatedTypeElements;
		} catch (Exception e) {
			reportNote("Could not load annotated elements cache (fine in case of full build)", e);
			return Collections.emptySet();
		}
	}

	private void generateDaggerModule(Set<TypeElement> annotatedElements) {
		reportNote("Entry points found: " + annotatedElements);
		Filer filer = processingEnv.getFiler();
		try {
			String packageName = "info.piwai.cleanandroidcode";
			String moduleClassName = "EntryPointsModule";
			JavaFileObject sourceFile = filer.createSourceFile(packageName + "." + moduleClassName);
			BufferedWriter writer = new BufferedWriter(sourceFile.openWriter());
			writer.write("package " + packageName + ";");
			writer.newLine();
			writer.write("import dagger.Module;");
			writer.newLine();
			writer.write("@Module(");
			writer.newLine();
			writer.write("  entryPoints = {");
			writer.newLine();

			for (TypeElement typeElement : annotatedElements) {
				String qualifiedName = typeElement.getQualifiedName().toString();
				String aaGeneratedQualifiedName = qualifiedName + "_";
				writer.write("    " + aaGeneratedQualifiedName + ".class,");
				writer.newLine();
			}

			writer.write("  },");
			writer.newLine();
			writer.write("  complete = false");
			writer.newLine();
			writer.write(")");
			writer.newLine();
			writer.write("public final class " + moduleClassName + " {");
			writer.newLine();
			writer.write("}");
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			reportNote("Could not generate Dagger module", e);
		}
	}

	private void cacheAnnotatedElements(Set<TypeElement> annotatedElements) {
		try {
			TypeElement[] annotatedElementsArray = annotatedElements.toArray(new TypeElement[0]);

			JavaFileManager.Location location = StandardLocation.SOURCE_OUTPUT;
			Filer filer = processingEnv.getFiler();
			FileObject resource = filer.createResource(location, CACHE_PACKAGE_NAME, CACHE_FILE_NAME, annotatedElementsArray);

			BufferedWriter writer = new BufferedWriter(resource.openWriter());
			writer.write("# This file has been generated, please do not edit it");
			writer.newLine();
			for (TypeElement element : annotatedElements) {
				writer.write(element.getQualifiedName().toString());
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			reportNote("Could not cache annotated elements " + annotatedElements.toString() + " (fine in case of full build)", e);
		}
	}

	private void reportError(String message) {
		processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
	}

	private void reportNote(String message) {
		processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
	}

	private void reportNote(String message, Throwable throwable) {
		processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message + "\nException:\n" + stackTraceToString(throwable));
	}

	private String stackTraceToString(Throwable throwable) {
		StringWriter writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		throwable.printStackTrace(pw);
		return writer.toString();
	}
}
