require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

PROVIDES += "u-boot"
DEPENDS += "lzop-native srecord-native bc-native dtc-native python3-pyelftools-native gnutls-native"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# u-boot source code repository
UBOOT_URL = "git://github.com/vudangRVC/u-boot-sst.git"
BRANCH = "fix/v4h-rz-cmn-bid-boot"
SRC_URI = "${UBOOT_URL};name=machine;protocol=https;branch=${BRANCH}"
SRC_URI:append:rz-cmn = " \
    ${@oe.utils.conditional('ENABLE_SPD_OPTEE', '1', 'file://0001-renesas-gen4-add-optee-bl32-fit-handoff.patch', '', d)} \
    ${@oe.utils.conditional('ENABLE_SPD_OPTEE', '1', 'file://0002-rz-cmn-boot-v4h-fit-when-present.patch', '', d)} \
"
SRCREV_machine = "254aa8a134f11302474bfc7378aa32f236a64602"

FILES:${PN} = "/boot ${sysconfdir}"

# Add the /boot directory to the target's sysroot
SYSROOT_DIRS += "/boot"

DEVICETREE_NAME:rz-cmn = " \
    rzg2l-sbc \
    smarc-rzg2l \
    smarc-rzv2l \
    rzv2h-evk-ver1 \
    rzv2h-rdk-ver1 \
    rs-g2l100 \
    imdt-v2h-sbc \
    r8a779g3-sparrow-hawk \
"

# Install u-boot-nodtb.bin and u-boot device tree to temp location
do_install() {
    install -d ${D}/boot
    install -d ${D}/boot/dtbs

    install -m 644 ${KCONFIG_CONFIG_ROOTDIR}/u-boot-nodtb.bin ${D}/boot/
    for dtb_name in ${DEVICETREE_NAME}; do
        install -m 644 ${KCONFIG_CONFIG_ROOTDIR}/dts/upstream/src/arm64/renesas/${dtb_name}.dtb ${D}/boot/dtbs
    done
}

do_deploy() {
    # Create deploy folder
    install -d ${DEPLOYDIR}/target/images/u-boot/dtbs

    install -m 0644 ${D}/boot/u-boot-nodtb.bin ${DEPLOYDIR}/target/images/u-boot/u-boot-nodtb-${MACHINE}.bin
    for dtb_name in ${DEVICETREE_NAME}; do
        install -m 644 ${D}/boot/dtbs/${dtb_name}.dtb ${DEPLOYDIR}/target/images/u-boot/dtbs
    done
}

addtask deploy after do_install
