# rz-cmn: the Renesas libgbm installs its header at ${includedir}/gbm/gbm.h
# (a subdir), not ${includedir}/gbm.h like mesa's gbm. glmark2's DRM backend
# does #include <gbm.h>, so add the gbm subdir to the include path.
CFLAGS:append:rz-cmn = " -I${STAGING_INCDIR}/gbm"
CXXFLAGS:append:rz-cmn = " -I${STAGING_INCDIR}/gbm"
