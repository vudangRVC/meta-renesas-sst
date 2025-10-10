require recipes-graphics/images/core-image-weston.bb
require include/core-image-renesas-base.inc
require ${@'include/core-image-renesas-mmp.inc' if d.getVar('RZ_FEATURE_CODEC') == 'True' else ''}
require include/core-image-bsp.inc
require include/core-image-renesas-cmn.inc
require include/renesas-qt5-framework.inc

SUMMARY = "Renesas core image with Qt5 platform support (no demo apps) based on core-image-weston"

IMAGE_INSTALL:append = " packagegroup-qt5 "
