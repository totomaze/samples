

/**
 * cette classe contient un main qui recherche tous les fichiers <nom_table>.table
 *  qui sont dans le m�me r�pertoire que le .jar et pour chaque fichier cr�e 
 *  un <nom_table>Base.java et <nom_table>BaseService.java. 
 *  Pour chaque morceau de chaque fichier, il y a une m�thode � declare� � qui rajoute ce morceau
 * @author dAlonso
 *
 */

public class GestionGenerator {
  public static String packageName="TA"; //exemple : GL
  public static String name="TAENF002_SaisiIntervention"; //Exemple : GLTAP606_PanierRubriquesTarification
  public static String suffixe=""; //Exemple : LST (GLTAP606_PanierRubriquesTarificationLST)
  public static String pathOutput = "D:\\Documents and Settings\\gThomazeau\\Mis documentos\\copyPaste\\afaire\\";//"D:\\projects\\workspace-ulisng\\maven.1285856976189\\src\\main\\java\\sopra\\immobilier\\business\\ULIS\\referentiel\\";
  public static String pathInput = "D:\\Documents and Settings\\gThomazeau\\Mis documentos\\copyPaste\\afaire\\";
  
  
  public GestionGenerator(String path, String packageName,String name,String suffixe){
    this.packageName=packageName;
    this.pathOutput=path;
    this.name=name;
    this.suffixe=packageName;
  }
  
  public static void main(String[] argv) throws Exception {
    FunctionNavigationGenerator function= new FunctionNavigationGenerator(pathInput,pathOutput,packageName, name, suffixe);
    FunctionNavigationGenerator.main(null);//generateFunctionForRecherche
    ServiceGestionGenerator serviceGenerator= new ServiceGestionGenerator(pathInput,pathOutput, packageName, name, suffixe);
    ServiceGestionGenerator.generateServiceForGestion();
    ScreenControllerGestionGenerator screenController= new ScreenControllerGestionGenerator(FunctionNavigationGenerator.getParametreEntre(),serviceGenerator.getListeCriteria(),serviceGenerator.getListeProxy(), pathOutput, packageName, name, suffixe);
    ScreenControllerGestionGenerator.generateScreenControllerClassForGestion();
    XHTMLGenerator xhmlGenerator= new XHTMLGenerator(pathInput,pathOutput, packageName, name, suffixe,ServiceGestionGenerator.getListeProxy(),ServiceGestionGenerator.getListeCriteria());
    XHTMLGenerator.generateXHTMLForGestion();
  }
  

  
}
