

## Otto

This project uses a custom version of Otto that replaces reflection with annotation processing and code generation.
It is based on a proof of concept from [Jake Wharton](https://github.com/JakeWharton), available on [this branch](https://github.com/square/otto/tree/code-gen) on the Square Otto repo.

You'll need to install the snapshot of the custom version of Otto on your local repo:
```
git submodule init
cd otto
mvn clean install -DskipTests
```