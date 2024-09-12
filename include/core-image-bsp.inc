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

DEPENDS += " linux-yocto"
