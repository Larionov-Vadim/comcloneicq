package layers.application;

/**
 ������ ��������, ������ �����, ������� ������
 */
public class AskingClass {

 private int infoStuff;
  /**
   *  ���� ��� 1 - ������ ����� �������� ���� (�� ���� �� ���������� � ���������� ���� � ������������ ������ ������� ����)
   *  ������� �������� ���� - ������ ����� ������ ���������� � ���� �������� ������
   *  ���� ��� 2 - ������ ��� ������ �� ������� ������� ������������, ���� ���� �� ����, ���� ������� "��������� �� �� ����"
   *  � ���� �� ��� ����� � ����, �� ���� �������, ��� �� ������ ��� �������� �� ����������� ��������
   *  ������� ��� ��� ��������� ���-�� ���� ������ ������ ��������
   *  ���� ��� 3 - ������, ��� ���-�� ���� ������ � ���� �� � ��� ������ ��������
   *
  */
 private String textMessage;

 //������ ��������� ���� 1

 public void setEnterInfoMessage(String message) {


  this.infoStuff = 1;
  this.textMessage = message;



 }

 //������ ��������� ���� 2
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
