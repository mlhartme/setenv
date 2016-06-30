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
