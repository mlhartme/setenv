# setup https://github.com/mlhartme/setenv

if [ -z ${SETENV_BASE} ] ; then
    export SETENV_BASE=/tmp/setenv-$$
    export SETENV_OLD_PROMPT_COMMAND=$PROMPT_COMMAND

    doSetenv() {
        eval "$SETENV_OLD_PROMPT_COMMAND"
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
