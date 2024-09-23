# Basic packages/tools for RZ/G2L-SBC
IMAGE_INSTALL:append = " \
    can-utils \
    i2c-tools \
    spitools \
    python3-smbus \
    python3-smbus2 \
    ethtool \
    iproute2 \
    iperf3 \
"

# Install overlays folder to root filesystem
add_overlays_rootfs() {
    install -d ${IMAGE_ROOTFS}/boot/overlays
    cp -r "${DEPLOY_DIR_IMAGE}/target/images/dtbs/overlays" "${IMAGE_ROOTFS}/boot/"
}

# Install rzpi.dtb to root filesystem
add_dtb_rootfs() {
    install -d ${IMAGE_ROOTFS}/boot
    install -m 0644 "${DEPLOY_DIR_IMAGE}/target/images/dtbs/rzpi.dtb" "${IMAGE_ROOTFS}/boot/"
}

ROOTFS_POSTPROCESS_COMMAND += " add_overlays_rootfs; add_dtb_rootfs"

do_rootfs[depends] += "linux-yocto:do_deploy"

DEPENDS += " firmware-pack"
