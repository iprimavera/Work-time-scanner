while true; do

  read codigo
  clear

  ya_trabajado=$(awk -F"," -v linea="$(date +%F)" -v columna="$codigo" '
  NR==1 {
    for(i=1; i<=NF; i++){
      if($i == columna){
        numerocolumna = i
        break
      }
    }
    next
  }
  {
    if($1 == linea) {
      print $numerocolumna
    }
  }' data.csv)

  tiempo_actual=$(date +%H%M%S)
  tiempo_ultimo=$(cat .registro_tmp.csv | grep $codigo | cut -d, -f3)

  numero=0
  for i in horas minutos segundos; do
    for j in actual ultimo; do
      eval "${i}_${j}=\"\${tiempo_${j}:${numero}:2}\""
    done
    numero=$((numero+2))
  done


  if [ $(cat .registro_tmp.csv | grep $codigo | cut -d, -f2) = "desconectado" ]; then

    # AL CONECTARSE

    tput setaf 2; echo "Bienvenid@ $(echo $nombre | cut -d" " -f-2)!"; tput sgr0
    tput setaf 5; echo "Hoy has trabajado $ya_trabajado hasta ahora"; tput sgr0
    sed -i "/$codigo/s/desconectado,$tiempo_ultimo/conectado,$tiempo_actual/" .registro_tmp.csv

    # CORREOS
    if [ $ya_trabajado = "0h0m0s" -a $(cat gecos.csv | grep $codigo | cut -d, -f4) = "y" ]; then
      ./email_sender.sh $correo $nombre
    fi

  else

    # AL DESCONECTARSE

    trabajado_horas=$((10#$horas_actual-10#$horas_ultimo)) # el 10# es para que sean base 10
    trabajado_minutos=$((10#$minutos_actual-10#$minutos_ultimo))
    trabajado_segundos=$((10#$segundos_actual-10#$segundos_ultimo))

    if [ $trabajado_segundos -lt 0 ]; then
      trabajado_minutos=$((trabajado_minutos-1))
      trabajado_segundos=$((60-(-trabajado_segundos)))
    fi
    if [ $trabajado_minutos -lt 0 ]; then
      trabajado_horas=$((trabajado_horas-1))
      trabajado_minutos=$((60-(-trabajado_minutos)))
    fi

    ya_trabajado_horas=$(echo $ya_trabajado | cut -d"h" -f1)
    ya_trabajado_minutos=$(echo $ya_trabajado | cut -d"m" -f1 | cut -d"h" -f2)
    ya_trabajado_segundos=$(echo $ya_trabajado | cut -d"s" -f1 | cut -d"m" -f2)

    for i in horas minutos segundos; do
      eval "trabajado_${i}=\$((trabajado_${i}+ya_trabajado_${i}))"
    done

    if [ $trabajado_segundos -gt 59 ]; then
      trabajado_minutos=$((trabajado_minutos+1))
      trabajado_segundos=$((trabajado_segundos-60))
    fi
    if [ $trabajado_minutos -gt 59 ]; then
      trabajado_horas=$((trabajado_horas+1))
      trabajado_minutos=$((trabajado_minutos-60))
    fi

    total="${trabajado_horas}h${trabajado_minutos}m${trabajado_segundos}s"

    gawk -i inplace -F"," -v linea="$(date +%F)" -v columna="$codigo" -v total="$total" '
    BEGIN {
      OFS=","
    }
    NR==1 {
      for(i=1; i<=NF; i++){
        if($i == columna){
          numerocolumna = i
          break
        }
      }
      print $0
      next
    }
    {
      if($1 == linea) {
        $numerocolumna = total
      }
      print $0
    }' data.csv

    tput setaf 1; echo "Hasta luego $(echo $nombre | cut -d" " -f-2)!"; tput sgr0
    tput setaf 5; echo "Hoy has trabajado $total hasta ahora"; tput sgr0
    # TODO poner cuanto tiempo le falta para completar las horas del dia
    sed -i "/$codigo/s/conectado,$tiempo_ultimo/desconectado,$tiempo_actual/" .registro_tmp.csv

  fi

done