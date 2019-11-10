let lista;
let valorTxt;
let xhr;
let mostrarLista = [];

if (localStorage.getItem("lista")){
  document.getElementById("resultado").innerHTML = localStorage.getItem("lista");
}

function buscar() {
  valorTxt = document.getElementById("buscador").value;
  if (valorTxt.length >= 3) {
        xhr = new XMLHttpRequest();
        xhr.onreadystatechange = process;
        xhr.open("GET", "https://raw.githubusercontent.com/David-Haim/CountriesToCitiesJSON/master/countriesToCities.json", true);
        xhr.send();
  }
}

function process()
{
  if (xhr.readyState == 4) {
    lista = JSON.parse(xhr.responseText);
    comparar();
  }
}

function buscaciudades(i){
  for (let j = 0; j < Object.values(lista)[i].length; j++) {
    if (Object.values(lista)[i][j].toUpperCase().includes(valorTxt.toUpperCase())) {
      var txt = Object.values(lista)[i][j].toUpperCase();
      var inicio = txt.indexOf(valorTxt.toUpperCase());
      var fin = valorTxt.length;

      var txtInicio = txt.substring(0, inicio);
      var txtRemarcar = txt.substring(inicio, inicio + fin);
      var txtFin = txt.substring(inicio + fin, txt.length);

      mostrarLista.push(txtInicio + "<mark>" + txtRemarcar + "</mark>" + txtFin);
    }
  }
}

function comparar() {
  for (let i = 0; i < Object.keys(lista).length; i++) {
    if (Object.keys(lista)[i].toUpperCase().includes(valorTxt.toUpperCase())) {
      var txt = Object.keys(lista)[i].toUpperCase();
      var inicio = txt.indexOf(valorTxt.toUpperCase());
      var fin = valorTxt.length;

      var txtInicio = txt.substring(0, inicio);
      var txtRemarcar = txt.substring(inicio, inicio + fin);
      var txtFin = txt.substring(inicio + fin, txt.length);
      mostrarLista.push(txtInicio + "<mark>" + txtRemarcar + "</mark>" + txtFin);
    }
    buscaciudades(i);
  }
  if (mostrarLista.length > 0) {
    for (var x = 0; x < mostrarLista.length; x++) {
      document.getElementById("resultado").innerHTML = document.getElementById("resultado").innerHTML + "<span class='texto'>" + mostrarLista[x] + "</span>" + "<br>";
    }
    localStorage.setItem("lista",mostrarLista);
  }
}