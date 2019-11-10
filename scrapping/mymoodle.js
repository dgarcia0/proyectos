var casper = require('casper').create();
var fs = require('fs');

casper.start('https://moodle.esliceu.com/login/index.php',function () {
    this.fill('form#login', {
        username: '',
        password:  ''
    }, true);

    casper.evaluate(function(){
        document.getElementById("#loginbtn").click();
    });

});

casper.wait(2000, function() {
    casper.capture('imagen.png');
    casper.evaluate(function(){
      document.querySelector('a#action-menu-toggle-0').click();
    });
});

casper.wait(2000,function () {
  casper.capture('imagen.png');
  casper.evaluate(function () {
    document.querySelector('#actionmenuaction-3').parentNode.click();
  });
});

casper.wait(2000,function () {
  casper.capture('imagen2.png');
  casper.evaluate(function () {
    document.querySelector('#grade-report-overview-2140_r3_c0 a').click();
  });
});

casper.wait(2000,function () {
  casper.capture('imagen3.png');
  var x = casper.evaluate(function () {
    return document.querySelector('table');
  });

  fs.write('tabla.html',x.innerHTML,'w');
});

casper.thenOpen('https://moodle.esliceu.com/mod/assign/view.php?id=46831', function() {
  casper.capture('imagen4.png');
  casper.evaluate(function () {
    document.querySelector('.mdl-left a').click();
  });
});

casper.wait(2000,function () {
  casper.capture('imagen5.png');
  casper.evaluate(function () {
    this.sendKeys('div.commentscontainer form textarea[name="content"]', "prueba");
  });
});

casper.wait(2000,function () {
  casper.capture('imagen6.png');
  casper.evaluate(function () {
    this.click('form input[type="submit"]');
  });
});

casper.run();