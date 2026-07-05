FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-u-dma-buf-support-kernel-6.18-ida-and-remove-api.patch \
"

INSANE_SKIP:${PN}:append = " buildpaths"
