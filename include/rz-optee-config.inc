# For Optee Features purpose

# Enable build and load of OP-TEE
ENABLE_SPD_OPTEE            ?= '0'
ENABLE_V4H_DIRECT_OPTEE     ?= '0'

# Direct V4H release inputs. Keep these immutable so the WIC, SPI loader and
# rootfs payload can be reproduced from the same source revisions.
V4H_DIRECT_UBOOT_SRCREV ?= "ee6d18cfa0e24747816778d3cc327733541ba4ec"
V4H_DIRECT_TFA_RZ_SRCREV ?= "b60ca2c677854755d7bfd24cca6320a5190b969c"
V4H_DIRECT_TFA_V4H_SRCREV ?= "1d5aa939bc8d3d892e2ed9945fa50e36a1a924cc"
V4H_DIRECT_OPTEE_RZ_SRCREV ?= "0327d33267abdef688588e1af8a134934d09563c"
V4H_DIRECT_OPTEE_V4H_SRCREV ?= "75e17800ed7e660fead6e3a2cb11c8e050dbbd87"
V4H_DIRECT_KERNEL_NONRT_SRCREV ?= "4c52297bc666b58c83efdb2a3951fede740c3abc"
V4H_DIRECT_KERNEL_RT_SRCREV ?= "7f689142d03da953a146f1de069571e2fa1c6bd8"

# Enable hardware crypt IP(SCE) driver in OP-TEE OS
ENABLE_RZ_SCE               ?= '0'

python () {
    if d.getVar('ENABLE_SPD_OPTEE') == '1' and d.getVar('ENABLE_V4H_DIRECT_OPTEE') == '1':
        bb.fatal('ENABLE_SPD_OPTEE and ENABLE_V4H_DIRECT_OPTEE are mutually exclusive')
}
