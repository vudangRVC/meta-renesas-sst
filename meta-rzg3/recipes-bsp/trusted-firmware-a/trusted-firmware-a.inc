FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

LIC_FILES_CHKSUM = " \
        file://docs/license.rst;md5=b2c740efedc159745b9b31f88ff03dde \
"

SRC_URI = " \
	git://github.com/renesas-rz/rzg_trusted-firmware-a.git;nobranch=1;protocol=https \
"

SRCREV = "0978650a03cc4af2c708b5ab9202c8257b5b890d"

S = "${WORKDIR}/git"

PV = "v2.7+git"
