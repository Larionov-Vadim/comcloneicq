package layers.application;

/**
 запрос каталога, запрос файла, запросы короче
 */
public class AskingClass {

 private int infoStuff;
  /**
   *  если тут 1 - значит хотят получить файл (то есть ты находишься в диалоговом окне а пользователь другой захотел файл)
   *  захотел полуичть файл - значит нажал кнопку отсылающую в окно передачи файлов
   *  если тут 2 - значит это запрос на наличие второго пользователя, типа если он есть, тебе говорят "приветики ты не один"
   *  а если он еще тупит в окне, то тебе говорят, что ты одинок как Киркоров на корпоративе РосНефти
   *  выходит что при включении кто-то один сначла энивей Киркоров
   *  если тут 3 - значит, что кто-то один свалил и тебе не с кем больше общаться
   *
  */
 private String textMessage;

 //Задать сообщение типа 1

 public void setEnterInfoMessage(String message) {


  this.infoStuff = 1;
  this.textMessage = message;



 }

 //Задать сообщение типа 2
 public void setFileAskingMessage(String message){

  this.infoStuff = 2;
  this.textMessage = message;

 }

 public void setExitMessage(String message){

  this.infoStuff = 3;
  this.textMessage = message;

 }

 public String giveMessage() {

  return (infoStuff+"   "+textMessage);


 }

}
