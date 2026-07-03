# rz-cmn: mesa-gl provides desktop OpenGL only (for xwayland/libepoxy), while
# the proprietary gles-user-module owns EGL/GLES and the Renesas libgbm owns
# virtual/libgbm. Two conflicts to resolve on this machine:
#
# 1. Both mesa-gl and gles-user-module ship ${includedir}/KHR/khrplatform.h,
#    which aborts do_prepare_recipe_sysroot — drop it from mesa-gl.
# 2. mesa.inc always declares libgbm/libgbm-dev packages and (via the gbm
#    PACKAGECONFIG) builds GBM, colliding with the Renesas libgbm recipe in
#    do_packagedata ("files already exist ... manifest-...-libgbm"). Disable
#    mesa's gbm and drop its libgbm packages so the Renesas libgbm is the sole
#    provider of virtual/libgbm.
PACKAGECONFIG:remove:rz-cmn = "gbm"
PACKAGES:remove:rz-cmn = "libgbm libgbm-dev"

do_install:append:rz-cmn() {
    rm -rf ${D}${includedir}/KHR
    rm -rf ${D}${libdir}/libgbm.* ${D}${libdir}/pkgconfig/gbm.pc ${D}${includedir}/gbm.h
}
