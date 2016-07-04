# setup https://github.com/mlhartme/setenv

if [ -z ${SETENV_BASE} ] ; then
    export SETENV_BASE=/tmp/setenv-$$

    doSetenv() {
        for file in ${SETENV_BASE}-*; do
            if [ -f "${file}" ] ; then
                . ${file}
                rm ${file}
            fi
        done
    }
    PROMPT_COMMAND=doSetenv
# else
#   already initialized, nothing to do
fi
