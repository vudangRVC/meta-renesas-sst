LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

B = "${WORKDIR}/build"

# Support to build dtbo for RZ SBC
KERNEL_DTC_FLAGS = "-@"
KERNEL_DEVICETREE_OVERLAY ?= ""

do_compile:prepend() {
    if [ -n "${KERNEL_DTC_FLAGS}" ]; then
       export DTC_FLAGS="${KERNEL_DTC_FLAGS}"
    fi
}

# Append steps to build the device tree overlays (dtbo)
do_compile:append() {
    for dtbf in ${KERNEL_DEVICETREE_OVERLAY}; do
        dtb=`normalize_dtb "$dtbf"`
        oe_runmake $dtb CC="${KERNEL_CC} $cc_extra " LD="${KERNEL_LD}" ${KERNEL_EXTRA_ARGS}
    done
}

# Install overlays folder and kernel images to target/images in build folder
do_deploy:append(){
    install -d ${DEPLOYDIR}/target/images/dtbs/overlays
    install -m 0644 ${B}/arch/arm64/boot/dts/renesas/overlays/* ${DEPLOYDIR}/target/images/dtbs/overlays

    install -m 0644 ${B}/arch/arm64/boot/Image ${DEPLOYDIR}/target/images/${base_name}.bin
    ln -sf ${base_name}.bin ${DEPLOYDIR}/target/images/Image

    install -m 0644 ${B}/arch/arm64/boot/dts/renesas/rzpi.dtb ${DEPLOYDIR}/target/images/dtbs/$dtb_base_name-${KERNEL_DTB_NAME}.$dtb_ext
    ln -sf $dtb_base_name-${KERNEL_DTB_NAME}.$dtb_ext ${DEPLOYDIR}/target/images/dtbs/rzpi.dtb
}

do_populate_sysroot () {
}

COMPATIBLE_MACHINE:rzg2l-sbc = "(rzg2l-sbc)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
