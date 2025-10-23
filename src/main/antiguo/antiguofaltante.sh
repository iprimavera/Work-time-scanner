while true; do

    # AL DESCONECTARSE


    tput setaf 1; echo "Hasta luego $(echo $nombre | cut -d" " -f-2)!"; tput sgr0
    tput setaf 5; echo "Hoy has trabajado $total hasta ahora"; tput sgr0
  fi

done