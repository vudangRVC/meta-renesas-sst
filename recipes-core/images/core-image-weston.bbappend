require include/core-image-renesas-base.inc
require include/core-image-bsp.inc
require ${@'include/core-image-renesas-mmp.inc' if d.getVar('RZ_FEATURE_CODEC') == 'True' else ''}
require include/core-image-renesas-cmn.inc