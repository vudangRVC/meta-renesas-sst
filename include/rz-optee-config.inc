# For Optee Features purpose

# Enable build and load of OP-TEE
ENABLE_SPD_OPTEE            ?= '0'
ENABLE_V4H_DIRECT_OPTEE     ?= '0'

# Immutable V4H payload inputs used by the manual U-Boot handoff contract.
V4H_DIRECT_TFA_V4H_SRCREV   ?= "1d5aa939bc8d3d892e2ed9945fa50e36a1a924cc"
V4H_DIRECT_OPTEE_RZ_SRCREV  ?= "7d4ae9c637772139b3fe6c1d601d1b71a2b93e2a"
V4H_DIRECT_OPTEE_V4H_SRCREV ?= "75e17800ed7e660fead6e3a2cb11c8e050dbbd87"
V4H_DIRECT_OPTEE_V4H_SOURCE_DATE_EPOCH ?= "1780644495"

V4H_DIRECT_BL31_SIZE        ?= "0x20040"
V4H_DIRECT_BL31_CRC32       ?= "af9584fe"
V4H_DIRECT_TEE_SIZE         ?= "0x65cd0"
V4H_DIRECT_TEE_CRC32        ?= "82b95c35"

# Enable hardware crypt IP(SCE) driver in OP-TEE OS
ENABLE_RZ_SCE               ?= '0'

python () {
    if d.getVar('ENABLE_SPD_OPTEE') == '1' and d.getVar('ENABLE_V4H_DIRECT_OPTEE') == '1':
        bb.fatal('ENABLE_SPD_OPTEE and ENABLE_V4H_DIRECT_OPTEE are mutually exclusive')
}
