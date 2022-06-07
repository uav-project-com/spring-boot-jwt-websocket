#!/bin/bash

# pass input example: Canhth0ng
# jdk-14 for OpenSSL 3.0.0-alpha11 28 jan 2021 (Library: OpenSSL 3.0.0-alpha11 28 jan 2021)
openssl pkcs12 -export -in fullchain1.pem -inkey privkey1.pem \
  -out springboot.p12 -name phicongtre.site -CAfile chain1.pem -caname root


