# Basic packages/tools
IMAGE_INSTALL:append = " \
    kernel-image \
    kernel-devicetree \
    bash \
    coreutils \
    can-utils \
    i2c-tools \
    spitools \
    python3-smbus \
    python3-smbus2 \
    ethtool \
    iproute2 \
    iperf3 \
"

# Install uEnv.txt to /boot
IMAGE_INSTALL:append:rzg2l-sbc = "uenv"

# Install overlays folder to root filesystem
add_rzsbc_overlays_rootfs() {
    install -d ${IMAGE_ROOTFS}/boot/overlays
    cp -r "${DEPLOY_DIR_IMAGE}/target/images/dtbs/overlays" "${IMAGE_ROOTFS}/boot/"
}

# Install rzpi.dtb to root filesystem
add_rzsbc_dtb_rootfs() {
    install -d ${IMAGE_ROOTFS}/boot
    install -m 0644 "${DEPLOY_DIR_IMAGE}/target/images/dtbs/rzpi.dtb" "${IMAGE_ROOTFS}/boot/"
}

ROOTFS_POSTPROCESS_COMMAND:rzg2l-sbc += " add_rzsbc_overlays_rootfs; add_rzsbc_dtb_rootfs"

do_rootfs[depends] += "linux-yocto:do_deploy"
