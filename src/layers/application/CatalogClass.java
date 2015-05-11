package layers.application;

import java.io.File;
import java.util.List;

/**
содержимое каталога
 */
public class CatalogClass {
    private List<String> fileCatalog;
    private int amount;
    private File path;

    public void setFileCatalog(List<String> catalog){

     this.fileCatalog = catalog;
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public List <String> getFileCatalog(){

        return this.fileCatalog;

    }



    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    public void GettingFileToMemory (String fileName, CatalogClass catalogMessage){

        


    }
}
