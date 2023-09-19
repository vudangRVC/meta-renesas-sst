LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " \
	file://0001-Fixed-an-issue-that-caused-flicker-when-outputting-t.patch \
	file://0003-workaround-boot-board.patch \
"

COMPATIBLE_MACHINE_rzg2l = "(smarc-rzg2l|rzg2l-dev|smarc-rzg2lc|rzg2lc-dev|smarc-rzg2ul|rzg2ul-dev|smarc-rzv2l|rzv2l-dev|rzpi)"

KERNEL_DEVICETREE_prepend = "renesas/rzpi.dtb"

# Remove the patch URI if it exists
SRC_URI_remove = "file://0002-Workaround-GPU-driver-remove-power-domains-of-GPU-no.patch \
		file://0002-Workaround-GPU-driver-remove-power-domains-v2l.patch \
"
