DESCRIPTION = "Linux Kernel for RZ SBC board"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/:"

KERNEL_URL = "git://github.com/vudangRVC/linux-rz-sst.git"
BRANCH = "dunfell/rz-sbc-rebase-3.0.6-update-3"
SRCREV = "${AUTOREV}"

SRC_URI = "${KERNEL_URL};protocol=https;nocheckout=1;branch=${BRANCH}"

SRC_URI_append = "\
	file://sii.cfg \
	file://laird.cfg \
"

