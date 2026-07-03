FILESEXTRAPATHS:prepend:rz-cmn := "${THISDIR}/${PN}:"

SRC_URI:append:rz-cmn = " file://drm-backend-remove-gbm-version-check.patch"

DEPENDS:append:rz-cmn= " libgbm"

RDEPENDS:${PN}:append:rz-cmn = " libgbm"

RDEPENDS:${PN}-examples:append:rz-cmn = " libgbm"

PACKAGECONFIG:remove:virtclass-multilib-lib32 = "launch"

EXTRA_OEMESON:append:rz-cmn = " -Dsimple-clients=egl,shm,damage,im,touch"
