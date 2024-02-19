DESCRIPTION = "OP-TEE OS"

LICENSE = "BSD-2-Clause & BSD-3-Clause"
LIC_FILES_CHKSUM = " \
        file://LICENSE;md5=c1f21c4f72f372ef38a5a4aee55ec173 \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "3.19.0+git${SRCPV}"
BRANCH = "3.19.0/rz"
#TAG: 3.19.0
SRC_URI = " \
	git://github.com/renesas-rz/rzg_optee-os.git;branch=${BRANCH} \
"

SRCREV = "f9a0900230a5341d9a83799dab89f08f65840458"

COMPATIBLE_MACHINE_rzg2l = "(smarc-rzg2l|rzg2l-dev|smarc-rzg2lc|rzg2lc-dev|smarc-rzg2ul|smarc-rzv2l|rzv2l-dev)"
COMPATIBLE_MACHINE_rzg2h = "(ek874|hihope-rzg2m|hihope-rzg2n|hihope-rzg2h)"
COMPATIBLE_MACHINE_rzg3s = "(rzg3s-dev|smarc-rzg3s)"

S = "${WORKDIR}/git"
