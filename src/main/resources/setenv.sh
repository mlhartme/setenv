# setup https://github.com/mlhartme/setenv

if [ -z ${SETENV_BASE} ] ; then
    if [ -n $ZSH_VERSION ] ; then
      export SETENV_BASE=/tmp/setenv-$$
      precmd() {
        # TODO: overwrites existing precmd ...
        for file in ${SETENV_BASE}-*(N); do
            if [ -f "${file}" ] ; then
                . ${file}
                rm ${file}
            fi
        done
      }
    elif [ -n $BASH_VERSION ] ; then
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
    else
      echo "error initializing setenv: unknown shell"
      exit
    fi

# else
#   already initialized, nothing to do
fi
