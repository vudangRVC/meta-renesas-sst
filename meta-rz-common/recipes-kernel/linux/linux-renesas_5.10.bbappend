DESCRIPTION = "Linux Kernel for RZ SBC board"

BB_STRICT_CHEKSUM:forcevariable = "0"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/:"

KERNEL_URL = "git://git@github.com/preetam-reddy/linux-rz-dev.git"
BRANCH = "rz-sbc-release1-5.10-cip36"
SRCREV = "${AUTOREV}"
SRC_URI = "${KERNEL_URL};protocol=ssh;nocheckout=1;branch=${BRANCH}"

SRC_URI_append = "\
	file://sii.cfg \
	file://laird.cfg \
"

