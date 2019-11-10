setInterval(async function() {
  let fetchito = await fetch("http://35.194.72.13/pra_WW.php")
  let result = await fetchito.json();
  postMessage(result);
},6000);


