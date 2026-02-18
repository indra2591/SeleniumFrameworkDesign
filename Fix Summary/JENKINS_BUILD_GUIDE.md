# Jenkins Build Configuration Guide

## Project: SeleniumFrameworkDesign2

### Maven Command
```bash
mvn clean test
```

### Jenkins Configuration

#### Build Step
**Type**: Invoke top-level Maven targets
**Goals**: `clean test`

#### Maven Configuration
- **Maven Version**: 3.9.11 (as per your current setup)
- **POM Location**: `pom.xml` (root of project)
- **JDK**: Java 25.0.2 (or compatible version)

### Test Suite Configuration

The project uses Maven Surefire Plugin with TestNG:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.5.4</version>
    <configuration>
        <suiteXmlFiles>
            <suiteXmlFile>testSuite/testng.xml</suiteXmlFile>
        </suiteXmlFiles>
    </configuration>
</plugin>
```

### Browser Configuration

- **Default Browser**: Chrome
- **Window Size**: 1920 x 1080 (configured in BaseTest)
- **WebDriver Manager**: Auto-configured via WebDriverManager

### Test Groups

- `purchase`: Submit order tests
- `ErrorHandling`: Error validation tests

### What Was Fixed

**Issue**: ElementClickInterceptedException on cart button click
- Element was positioned at negative Y-coordinate (-145)
- Outside visible viewport in Jenkins environment

**Solution**: 
- Added JavaScript scrollIntoView before clicking
- Implemented JavaScript click for reliability
- Added fallback to standard click

### Expected Test Results

Total: 5 tests
- **Pass**: All tests should pass
- **Failures**: 0 (previously 3)
- **Skipped**: Based on test dependencies

### Reports Location

After build:
- **Surefire Reports**: `target/surefire-reports/`
- **TestNG Reports**: `test-output/`
- **Extent Reports**: `reports/index.html`

### Troubleshooting

#### If build still fails:

1. **Check Chrome version**: Ensure ChromeDriver is compatible
   ```
   WARNING: Unable to find CDP implementation matching 145
   ```
   This is just a warning, not a failure.

2. **Verify Maven settings**: Ensure Maven can access dependencies

3. **Check workspace permissions**: Jenkins needs read/write access

4. **Browser installation**: Chrome must be installed on Jenkins agent

5. **Network issues**: Ensure test application URL is accessible

### Running Specific Tests

To run specific test groups:
```bash
mvn clean test -Dgroups=purchase
mvn clean test -Dgroups=ErrorHandling
```

To run with specific browser:
```bash
mvn clean test -Dbrowser=chrome
mvn clean test -Dbrowser=firefox
mvn clean test -Dbrowser=edge
```

### Post-Build Actions (Recommended)

1. **Archive artifacts**: 
   - `reports/**/*.html`
   - `target/surefire-reports/**/*`
   - `test-output/**/*`

2. **Publish TestNG Results**:
   - TestNG XML report pattern: `**/testng-results.xml`

3. **Publish HTML Reports**:
   - Report directory: `reports`
   - Index page: `index.html`

### Success Indicators

✅ Build Status: SUCCESS
✅ Tests run: 5
✅ Failures: 0
✅ Errors: 0
✅ Skipped: 1 (expected due to dependencies)

---
**Updated**: February 13, 2026
**Status**: Ready for Jenkins deployment
