export SETENV_BASE=/tmp/setenv-$$

doSetenv() {
    for name in ${SETENV_BASE}-*; do
        [ -f "${name}" ] || break
        echo "source $file"
    done
}

PROMPT_COMMAND=doSetenv
