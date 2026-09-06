@echo off
set KEYSTORE=keystore.p12
set PASSWORD=changeit
set ALIAS=tomcat

keytool -genkeypair ^
    -alias %ALIAS% ^
    -keyalg RSA ^
    -keysize 2048 ^
    -storetype PKCS12 ^
    -keystore %KEYSTORE% ^
    -storepass %PASSWORD% ^
    -keypass %PASSWORD% ^
    -validity 3650 ^
    -dname "CN=localhost, OU=Development, O=Organization, L=City, ST=State, C=US"

echo Keystore generated successfully!
pause 