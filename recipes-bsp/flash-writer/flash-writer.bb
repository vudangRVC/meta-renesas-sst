LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"
LICENSE = "BSD-3-Clause"

PV = "0.6"
PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(rzv2h-evk-ver1)"

UNPACKDIR = "${S}"

inherit deploy

# Source file location
SRC_URI = "file://Flash_Writer_SCIF_RZV2H_DEV_INTERNAL_MEMORY.mot"
SRC_URI[md5sum] = "33ac1ccb33a07f574255b4329feb02a9"
SRC_URI[sha256sum] = "90d62cf98015bdb6489461ca8bb5cb458c261fe0b5fa555170e45f0aca8ca244"

# File extraction paths
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Disable compiling and installing as we only need to deploy the file
do_compile[noexec] = "1"
do_install[noexec] = "1"

# Custom deploy task for deploying the .mot file
do_deploy() {
	install -d ${DEPLOYDIR}/target/images
	install -m 755 ${S}/Flash_Writer_SCIF_RZV2H_DEV_INTERNAL_MEMORY.mot ${DEPLOYDIR}/target/images/
}

# Set parallel make settings
PARALLEL_MAKE = "-j 1"

# Add the deploy task after compilation
addtask deploy after do_compile