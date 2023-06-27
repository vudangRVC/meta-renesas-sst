DESCRIPTION = "Linux Kernel for RZ SBC board"

KERNEL_URL = "https://github.com/preetam-reddy/linux-rz-dev.git"
BRANCH = "dunfell/rz-sbc"
SRCREV = "${AUTOREV}"
SRC_URI = "${KERNEL_URL};protocol=https;nocheckout=1;branch=${BRANCH}"

SRC_URI_append = "\
  file://sii.cfg \
"

