DESCRIPTION = "Linux Kernel for RZ SBC board"

BB_STRICT_CHEKSUM:forcevariable = "0"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/:"

KERNEL_URL = "git://git@github.com/preetam-reddy/linux-rz-dev.git"
BRANCH = "rz-sbc-release3-5.10-cip36"
SRCREV = "bd334f8cea81e68f650142f0534bbbb861bffa54"
SRC_URI = "${KERNEL_URL};protocol=ssh;nocheckout=1;branch=${BRANCH}"

SRC_URI_append = "\
	file://0001-drivers-bluetooth-usb-add-support-for-UB500.patch \
	file://ext-bluetooth.cfg \
	file://sii.cfg \
	file://laird.cfg \
"

