

/**
 * cette classe contient un main qui recherche tous les fichiers <nom_table>.table
 *  qui sont dans le m�me r�pertoire que le .jar et pour chaque fichier cr�e 
 *  un <nom_table>Base.java et <nom_table>BaseService.java. 
 *  Pour chaque morceau de chaque fichier, il y a une m�thode � declare� � qui rajoute ce morceau
 * @author dAlonso
 *
 */

public class SyntheseGenerator {
  public static String packageName="FS"; //exemple : GL
  public static String name="FSSLY001_OccPreSaisieEnquete"; //Exemple : GLTAP606_PanierRubriquesTarification
  public static String suffixe="SYN"; //Exemple : LST (GLTAP606_PanierRubriquesTarificationLST)
  public static String pathOutput = "D:\\Documents and Settings\\gThomazeau\\Mis documentos\\copyPaste\\afaire\\";//"D:\\projects\\workspace-ulisng\\maven.1285856976189\\src\\main\\java\\sopra\\immobilier\\business\\ULIS\\referentiel\\";
  public static String pathInput = "D:\\Documents and Settings\\gThomazeau\\Mis documentos\\copyPaste\\afaire\\";
  
  
  public SyntheseGenerator(String path, String packageName,String name,String suffixe){
    this.packageName=packageName;
    this.pathOutput=path;
    this.name=name;
    this.suffixe=packageName;
  }
  
  public static void main(String[] argv) throws Exception {
    ServiceGenerator serviceGenerator= new ServiceGenerator(pathInput,pathOutput, packageName, name, suffixe);
    ServiceGenerator.generateServiceForSynthesis();
    
    /*XHTMLGenerator xhmlGenerator= new XHTMLGenerator(pathInput,pathOutput, packageName, name, suffixe,ServiceGenerator.getListeProxy(),ServiceGenerator.getListeCriteria());
    XHTMLGenerator.generateXHTMLForRecherche();*/
  }
  
}
