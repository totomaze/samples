
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Gthomazeau
 *
 */

public class ScreenControllerGenerator {
  public static String packageName="GL"; //exemple : GL
  public static String name="GLTAP607_PanierGroupeDePrix"; //Exemple : GLTAP606_PanierRubriquesTarification
  public static String suffixe="LST"; //Exemple : LST (GLTAP606_PanierRubriquesTarificationLST)
  public static String pathOutput = "D:\\Ulis\\generateur\\BaseServiceGenerator\\src\\";//"D:\\projects\\workspace-ulisng\\maven.1285856976189\\src\\main\\java\\sopra\\immobilier\\business\\ULIS\\referentiel\\";
  public static String pathInput = "D:\\Ulis\\generateur\\BaseServiceGenerator\\src\\";
  public static List<ParametreEntree> listeParamEntree =null;
  public static List<Proxy> listeProxy =null;
  public static List<Criteria> listeCriteria =null;
  
  public ScreenControllerGenerator(List<ParametreEntree> paramEntree,List<Criteria> paramCriteria,List<Proxy> paramProxy,String pathOutput, String packageName,String name,String suffixe){
    this.listeParamEntree=paramEntree;
    this.listeCriteria=paramCriteria;
    this.listeProxy=paramProxy;
    this.packageName=packageName;
    this.pathOutput=pathOutput;
    this.name=name;
    this.suffixe=suffixe;
  }
  
  public static void main(String[] argv) throws Exception {
   
  }
  
   
public static void generateScreenControllerClassForPanier(){
    
    try {
      File file = new File(pathOutput+name+suffixe+"ScreenController.java");
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
      out.append(declarePackage(packageName));
      out.append(declareImportClass(listeParamEntree));
      out.append(declareClassCommentary());
      out.append(declareClassForPanier());
      out.append(declareClassConstructeurForPanier());
      out.append(declareProcessInitializeClassForPanier(listeParamEntree,listeProxy));
      out.append(declareProcessValidateForPanier(listeParamEntree));
      out.append(declareInsertAllForPanier());
      out.append(declareSearchForPanier());
      out.append(declareSelectForPanier());
      out.append(declareGetFunctionClass());
      out.append(declareBean());
      out.append(declareLSTEntityControllerForPanier());
      out.append(declareCriteriaEntityControllerForPanier(listeCriteria));
      
      out.append("}//fin classe ").append(name+suffixe).append("ScreenController");
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  

public static void generateScreenControllerClassForRecherche(){
  
  try {
    File file = new File(pathOutput+name+suffixe+"ScreenController.java");
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
    out.append(declarePackage(packageName));
    out.append(declareImportClassForRecherche(listeParamEntree));
    out.append(declareClassCommentary());
    out.append(declareClassForRecherche());
    out.append(declareClassConstructeurForRecherche());
    
    out.append(declareProcessInitializeClassForRecherche(listeParamEntree, listeProxy));
    out.append(processReset());
    out.append(_resetCriteria(listeCriteria));
    
    //out.append(selectForRecherche());
    out.append(getSynthesisCallForRecherche());
    out.append(declareSearchForRecherche());
    out.append(processCancel());
    out.append(declareGetFunctionClass());

    out.append(declareBean());
    
    out.append(declareCriteriaEntityControllerForRecherche(listeCriteria));
    
    out.append("}//fin classe ").append(name+suffixe).append("ScreenController");
    out.close();
  } catch (Exception e) {
    e.printStackTrace();
  } 
}

  private static String declareClassCommentary() {
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * Description :\n");
    sb.append(" * ScreenController ").append(name+suffixe).append("\n");
    sb.append(" *\n");
    sb.append(" * <TABLE>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><B>Attributs SVN :</B></TD>\n");
    sb.append(" *     <TD>&nbsp;</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><I>Nom du fichier :</I></TD>\n");
    sb.append(" *     <TD>$HeadURL: \n");
    sb.append(" *   </TR>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><I>Modifié le :</I></TD>\n");
    sb.append(" *     <TD>$Date: 2010-10-18 18:14:44 +0200 (lun, 18 oct 2010) $</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append(" * </TABLE>\n");
    sb.append(" *\n");
    sb.append(" * @version : $Revision: 1 $\n");
    sb.append(" * @author  : SOPRA Group - $Author: gthomazeau $\n");
    sb.append(" *\n");
    sb.append(" */\n");
    return sb.toString();
  }

  private static String declareClassForPanier() {
    StringBuilder sb = new StringBuilder();
    sb.append("@ManagedBean(name = \""+packageName+"_"+name+suffixe+ "_screenController\") \n");
    sb.append("@ViewScoped \n");
    sb.append("public class ").append(name+suffixe).append("ScreenController extends AbstractShoppingCartScreenController<"+name+suffixe+"ScreenController."+name+"Bean,");
    sb.append(name+suffixe+"ScreenController."+name+"Bean> {\n");
    sb.append("  //Constantes\n");
    sb.append("  /** Version extraite du gestionnaire de configuration */\n");
    sb.append("  @SuppressWarnings(\"hiding\")\n");
    sb.append("  public static final String CLASS_VERSION = \"$Revision: $\";\n");
    sb.append("  /** Nom de l'entité pour la liste des résultats */\n");
    sb.append("  public static final String LISTE_RESULTATS_ENTITY_NAME = \"listeResultats\";\n");
    sb.append("  /** Nom de l'entité pour les critères de recherche */\n");
    sb.append("  public static final String CRITERIA_ENTITY_NAME = \"rechCriteria\";\n");
    return sb.toString();
  }

  private static String declareClassForRecherche() {
    StringBuilder sb = new StringBuilder();
    sb.append("@ManagedBean(name = \""+packageName+"_"+name+suffixe+ "_screenController\") \n");
    sb.append("@ViewScoped \n");
    sb.append("public class ").append(name+suffixe).append("ScreenController extends AbstractInOutSearchScreenController<"+name+suffixe+"ScreenController."+name+"Bean");
    sb.append("> {\n");
    sb.append("  //Constantes\n");
    sb.append("  /** Version extraite du gestionnaire de configuration */\n");
    sb.append("  @SuppressWarnings(\"hiding\")\n");
    sb.append("  public static final String CLASS_VERSION = \"$Revision: $\";\n");
    sb.append("  /** Nom de l'entité pour la liste des résultats */\n");
    sb.append("  public static final String LISTE_RESULTATS_ENTITY_NAME = \"listeResultats\";\n");
    sb.append("  /** Nom de l'entité pour les critères de recherche */\n");
    sb.append("  public static final String CRITERIA_ENTITY_NAME = \"rechCriteria\";\n");
    
    sb.append("/** Nom de l'entité constructeurs */\n");
    sb.append("private  String entityName = null;\n");
    
    sb.append("/** Lots sélectionné au sein de la liste */\n");
    sb.append("private "+name+"Bean mSelectedOccurrence = null;\n");
    
    sb.append("/**\n");
    sb.append(" * Le contrôleur de l'entité des critère de recherche\n");
    sb.append(" * \n");
    sb.append(" * @return le contrôleur demandé\n");
    sb.append(" * @throws ModelException erreur technique survient lors d'un traitement dans le modèle interne\n");
    sb.append(" */\n");
    sb.append(" protected BaseEntity<"+name+"CriteriaBean> getCriteriaEntity() throws ModelException {\n");
    sb.append("   return getModel().getBaseEntity(CRITERIA_ENTITY_NAME, "+name+"CriteriaBean.class);\n");
    sb.append(" }\n");
    sb.append(" \n");
    sb.append(" /**\n");
    sb.append("  * Le contrôleur de l'entité lot d'encaissement\n");
    sb.append("  * \n");
    sb.append("  * @return le contrôleur demandé\n");
    sb.append("  * @throws ModelException erreur technique survient lors d'un traitement dans le modèle interne\n");
    sb.append("  */\n");
    sb.append(" protected BaseEntity<"+name+"Bean> getListeEntity() throws ModelException {\n");
    sb.append("   return getModel().getBaseEntity(entityName, "+name+"Bean.class);\n");
    sb.append(" }\n");
    sb.append("/**\n");
    sb.append(" * Recuperation de l'occurence\n");
    sb.append(" * \n");
    sb.append(" * @return la valeur positionnée au sein de la propriété\n");
    sb.append(" */\n");
    sb.append("public "+name+"Bean getSelectedOccurrence() {\n");
    sb.append("  return  ModelUtils.getInstance().getCurrentModelOccurrence(FacesContext.getCurrentInstance(),\n");
    sb.append("                                                            "+name+"Bean.class);\n");
    sb.append(" }  \n");
    sb.append(" /**\n");
    sb.append(" * Setter de l'occurence selectionne\n");
    sb.append(" * @param selectedOccurrence valeur à positionner au sein de la propriété\n");
    sb.append(" */\n");
    sb.append(" public void setSelectedOccurrence("+name+"Bean selectedOccurrence) {\n");
    sb.append("   if (isLoggingDebug())\n");
    sb.append("     logDebug(\"setSelectedOccurrence() <-- \" + selectedOccurrence);\n");
    sb.append("   //\n");
    sb.append("   mSelectedOccurrence = selectedOccurrence;\n");
    sb.append(" }\n");
    return sb.toString();
  }
  
  private static String declareClassConstructeurForPanier() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("/** \n");
    sb.append(" * Constructeur du screenControlleur panier \n");
    sb.append(" */\n");
    sb.append("public "+name+suffixe+"ScreenController() {\n");
    sb.append("  super("+name+"Bean.class,\n");
    sb.append("        "+name+"Bean.class);\n");
    sb.append("}\n");
    return sb.toString();
  } 
  
  private static String declareClassConstructeurForRecherche() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("/**\n");
    sb.append("  * Constructeur\n");
    sb.append("  */\n");
    sb.append(" public "+name+suffixe+"ScreenController() {\n");
    sb.append("   super("+name+"Bean.class);\n");
    sb.append("   \n");
    sb.append("   if (isLoggingDebug()) {\n");
    sb.append("     logDebug(\""+name+suffixe+"ScreenController#"+name+suffixe+"ScreenController\");\n");
    sb.append("   }\n");
    sb.append("  \n");
    sb.append("  //\n");
    sb.append("  // on récupère la définition du bloc de résultats\n");
    sb.append(" BlocMetierProxy blocResultats = null;\n");
    sb.append("  try {\n");
    sb.append("    blocResultats = PersonalizationUtils.getInstance()\n");
    sb.append("                                        .retrievePersonalizedSearchResultBlock(FacesContext.getCurrentInstance());\n");
    sb.append("  }\n");
    sb.append("  catch (final ServletException sEx) {\n");
    sb.append("    final ModelException mex = new ModelException(sEx.getMessage(), sEx);\n");
    sb.append("    if (isLoggingError()) {\n");
    sb.append("      logError(mex.getMessage(), mex);\n");
    sb.append("    }\n");
    sb.append(" }\n");
    sb.append(" // on crée une entité multi-occurrence dont le nom correspond au code de l'ensemble de données associé au\n");
    sb.append("  // bloc résultat de la recherche\n");
    sb.append("  if (blocResultats != null) {\n");
    sb.append("    entityName = blocResultats.getCOD();\n");
    sb.append("  }\n");
    sb.append("  \n");
    sb.append("}\n");
    return sb.toString();
  }  

  private static ArrayList<ParametreEntree> gettingDataFromFile(String path,String nomFichier) {
    ArrayList<ParametreEntree> dataList = new ArrayList<ParametreEntree>();
    try { 
      BufferedReader in = new BufferedReader(new FileReader(path+nomFichier)); 
      String str; 
      
      while ((str = in.readLine()) != null) { 
        ParametreEntree data = new ParametreEntree(str);
        dataList.add(data);
      } 
      in.close(); 
      
    } catch (FileNotFoundException e) {e.printStackTrace(); } 
    catch (IOException ioe){}
    return dataList;      
  }

  

  private static String declareClasseDeclaration(String tableName) {
    StringBuilder sb = new StringBuilder();
    sb.append("public class ").append(tableName).append("Base extends AbstractBusinessObject {\n");
    sb.append("  //Constantes\n");
    sb.append("  /** Version extraite du gestionnaire de configuration */\n");
    sb.append("  @SuppressWarnings(\"hiding\")\n");
    sb.append("  public static final String CLASS_VERSION = \"$Revision: $\";\n");
    sb.append("  //Attributs\n");
    return sb.toString();
  }

  
  private static String declareImportClass(List<ParametreEntree> dataList) {
    StringBuilder sb = new StringBuilder();
    
    sb.append("import java.util.Iterator;\n");
    sb.append("import java.util.List;\n");
    sb.append("import java.util.Map;\n");
    sb.append("import javax.faces.bean.ManagedBean;\n");
    sb.append("import javax.faces.bean.ViewScoped;\n");
    sb.append("import javax.faces.context.FacesContext;\n");
    sb.append("import sopra.core.bean.CoreBeanException;\n");
    sb.append("import sopra.core.bean.NaturalOrderPropertyComparator;\n");
    sb.append("import sopra.faces.bean.DefaultAction;\n");
    sb.append("import sopra.faces.util.FacesUtils;\n");
    sb.append("import sopra.immobilier.business.ULIS."+packageName+"."+name+"Proxy;\n");
    sb.append("import sopra.immobilier.business.ULIS."+packageName+"."+name+"Service;\n");
    sb.append("import sopra.immobilier.model.AbstractShoppingCartScreenController;\n");
    sb.append("import sopra.immobilier.model.BaseEntity;\n");
    sb.append("import sopra.immobilier.model.InMemorySort;\n");
    sb.append("import sopra.immobilier.model.ModelBean;\n");
    sb.append("import sopra.immobilier.model.ModelException;\n");
    sb.append("import sopra.immobilier.model.OccurrenceStatus;\n");
    sb.append("import sopra.immobilier.model.ServiceBasedSort;\n");
    sb.append("import sopra.immobilier.model.ServiceBasedStorageModule;\n");
    sb.append("import sopra.immobilier.model.ShoppingCartEntityController;\n");
    sb.append("import sopra.immobilier.model.StorageStrategy;\n");
    sb.append("import sopra.immobilier.navigation."+packageName+"."+name+suffixe+"Function;\n");
    sb.append("import sopra.immobilier.service.business.ServiceCriteria;\n");
    sb.append("import sopra.immobilier.service.business.ServiceException;\n");
    sb.append("import sopra.immobilier.service.business.ServiceInputOutputFactory;\n");
    sb.append("import sopra.immobilier.service.business.ServiceResponse;\n");
    sb.append("import sopra.navigation.AbstractFunctionCall;\n");
    sb.append("import sopra.navigation.NavigationException;\n");
    sb.append("import sopra.navigation.connectors.faces.FacesNavigationConnector;\n");
    sb.append("\nimport sopra.immobilier.business.ULIS.AbstractBusinessObject;\n");
    if(contains(dataList,"BigDecimal"))
      sb.append("import java.math.BigDecimal;\n");
    if(contains(dataList,"Date"))
      sb.append("import java.util.Date;\n");
    System.out.println("Don't forget to open the result file on eclipse and check import.");
    return sb.toString();   
  }
  
  private static String declareImportClassForRecherche(List<ParametreEntree> dataList) {
    StringBuilder sb = new StringBuilder();
    
    sb.append("import java.util.Iterator;\n");
    sb.append("import java.util.List;\n");
    sb.append("import java.util.Map;\n");
    sb.append("import javax.faces.bean.ManagedBean;\n");
    sb.append("import javax.faces.bean.ViewScoped;\n");
    sb.append("import javax.faces.context.FacesContext;\n");
    sb.append("import sopra.core.bean.CoreBeanException;\n");
    sb.append("import sopra.core.bean.NaturalOrderPropertyComparator;\n");
    sb.append("import sopra.faces.bean.DefaultAction;\n");
    sb.append("import sopra.faces.util.FacesUtils;\n");
    sb.append("import sopra.immobilier.business.ULIS."+packageName+"."+name+"Proxy;\n");
    sb.append("import sopra.immobilier.business.ULIS."+packageName+"."+name+"Service;\n");
    sb.append("import sopra.immobilier.model.AbstractShoppingCartScreenController;\n");
    sb.append("import sopra.immobilier.model.AbstractPersonalizationCriteriaEntityController;\n");
    sb.append("import sopra.immobilier.model.AbstractSearchScreenController;\n");
    sb.append("import sopra.immobilier.model.ModelUtils;\n");
    sb.append("import sopra.immobilier.personalization.service.BlocMetierProxy;\n");
    sb.append("import sopra.immobilier.personalization.faces.PersonalizationUtils;\n");
    sb.append("import javax.servlet.ServletException;\n");
    sb.append("import sopra.immobilier.service.user.ULIS.ULISUserProfile;\n");
    sb.append("import sopra.immobilier.faces.referentiel.PA.PAESICriterionBean;\n");
    sb.append("import sopra.core.util.DateTools;\n");
    sb.append("import sopra.immobilier.model.BaseEntity;\n");
    sb.append("import sopra.immobilier.model.InMemorySort;\n");
    sb.append("import sopra.immobilier.model.ModelBean;\n");
    sb.append("import sopra.immobilier.model.ModelException;\n");
    sb.append("import sopra.immobilier.model.OccurrenceStatus;\n");
    sb.append("import sopra.immobilier.model.ServiceBasedSort;\n");
    sb.append("import sopra.immobilier.model.ServiceBasedStorageModule;\n");
    sb.append("import sopra.immobilier.model.ShoppingCartEntityController;\n");
    sb.append("import sopra.immobilier.model.StorageStrategy;\n");
    sb.append("import sopra.immobilier.navigation."+packageName+"."+name+suffixe+"Function;\n");
    sb.append("import sopra.immobilier.service.business.ServiceCriteria;\n");
    sb.append("import sopra.immobilier.service.business.ServiceException;\n");
    sb.append("import sopra.immobilier.service.business.ServiceInputOutputFactory;\n");
    sb.append("import sopra.immobilier.service.business.ServiceResponse;\n");
    sb.append("import sopra.navigation.AbstractFunctionCall;\n");
    sb.append("import sopra.navigation.NavigationException;\n");
    sb.append("import sopra.navigation.connectors.faces.FacesNavigationConnector;\n");
    sb.append("\nimport sopra.immobilier.business.ULIS.AbstractBusinessObject;\n");
    if(contains(dataList,"BigDecimal"))
      sb.append("import java.math.BigDecimal;\n");
    if(contains(dataList,"Date"))
      sb.append("import java.util.Date;\n");
    System.out.println("Don't forget to open the result file on eclipse and check import.");
    return sb.toString();   
  }

  private static String declarePackage(String packageName) {
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n").append("* Paquet de définition\n").append("*/\n");
    sb.append("package sopra.immobilier.faces."+packageName+"; \n\n");
    return sb.toString();
  }
  
  private static String declareProcessInitializeClassForPanier(List<ParametreEntree> paramEntree,List<Proxy> listeProxy){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * @see sopra.immobilier.model.AbstractShoppingCartScreenController#processInitializeModel() \n");
    sb.append("*/ \n");
    sb.append("@Override \n");
    sb.append("protected void processInitializeModel() throws ModelException { \n");
    sb.append(" super.processInitializeModel(); \n");
    sb.append("\n");
    sb.append("  final "+name+suffixe+"Function"+" function = ("+name+suffixe+"Function"+") getModel().getCurrentFunctionCall(); \n");
    sb.append("  \n");
    sb.append("  \n");
    
    sb.append(initializeCriteria());
    
    sb.append("\n");
    sb.append("    // \n");
    sb.append("    // ===> RESULTAT DE RECHERCHE \n"); 
    sb.append("    // \n");
    sb.append("    final BaseEntity<"+name+"Bean"+"> listeEntity = new BaseEntity<"+name+"Bean"+">(LISTE_RESULTATS_ENTITY_NAME, \n");
    sb.append("                                                                                                                                     getModel(), \n");
    sb.append("                                                                                                                                     "+name+suffixe+"ScreenController."+name+"Bean.class"+"); \n");
    sb.append("    // On définit l'entité comme étant multi-occurrence \n");
    sb.append("    listeEntity.setDefaultBlockSize(10); \n");
    sb.append("    listeEntity.setEntityEditable(false); \n");
    sb.append("    // Création d'un contrôleur d'entité ESI vacants \n");
    sb.append("    listeEntity.createEntityController(StorageStrategy.RANDOM_READ_SERVICE_BASED, \n");
    sb.append("                                       "+name+"Service.SERVICE_NAME); \n");
    sb.append("    // On définit les tris possibles sur les colonnes \n");
    sb.append("                                       \n");  
    for (Proxy proxy : listeProxy) {
      if (proxy.isTriable()){
        sb.append("    listeEntity.addAvailableSorts(ServiceBasedSort.createDualColumnSort(\""+proxy.getNom()+"\", \""+proxy.getNom()+"\")); \n");
      }
    }

    sb.append("\n");
    sb.append("    listeEntity.getController().getOccurrenceGroupStorageModule().createEmptyOccurrenceGroup(null); \n");
    sb.append("    getModel().setShowSearchResults(true); \n");
    sb.append("    \n");
    sb.append("    // ===> PANIER DE SELECTION \n");
    sb.append("    // On se contente ici de paramétrer le panier \n");
    sb.append("    // \n");
    sb.append("    getShoppingCart().getEntity().setEntityEditable(false); \n");
    sb.append("    getShoppingCartController().setSupportDuplicateItems(false); \n");
    sb.append("    \n");        

    for (Proxy proxy : listeProxy) {
      if (proxy.isTriable()){
        sb.append("    getShoppingCart().getEntity().addAvailableSorts(InMemorySort.createDualColumnSort(\""+proxy.getNom()+"\",new NaturalOrderPropertyComparator<"+name+"Bean"+", String>(\""+proxy.getNom()+"\")).values()); \n");
      }
    }
    
    sb.append(initializeDonneeForPanier());      
    
    boolean ajouterFillShoppingCart=false;
    ParametreEntree paramPanier=null;
    //recuperation des parametres d'entrees
    for (ParametreEntree param : listeParamEntree) {
      //recuperation de la liste du panier contenu dans la fonction de navigation
      if (param.isEntree() && param.getType().contains("List") && !ajouterFillShoppingCart){ //traitement de la premiere liste en entreé pour le panier
       sb.append("// Remplissage du panier avec les demandes en paramètre\n");
       sb.append("\n");
       sb.append("fillShoppingCart();\n");
       ajouterFillShoppingCart=true;
       paramPanier=param;
      }else{
        //Parametre normal
        //@TODO traiter les parametre d'entré normaux
      }
    }
    sb.append("getModel().setShowSearchResults(false);\n");
    sb.append("    \n");        
    sb.append("    \n");      
    sb.append("} \n");
    
    if (ajouterFillShoppingCart){
      sb.append(declareFillShoppingCart(paramPanier));
    }
    
    return sb.toString(); 
  }
  
  private static String declareProcessInitializeClassForRecherche(List<ParametreEntree> paramEntree,List<Proxy> listeProxy){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * @see sopra.immobilier.model.AbstractShoppingCartScreenController#processInitializeModel() \n");
    sb.append("*/ \n");
    sb.append("@Override \n");
    sb.append("protected void processInitializeModel() throws ModelException { \n");
    sb.append(" super.processInitializeModel(); \n");
    sb.append("\n");
    sb.append("  final "+name+suffixe+"Function"+" function = ("+name+suffixe+"Function"+") getModel().getCurrentFunctionCall(); \n");
    sb.append("  \n");
    sb.append("  \n");
    
    sb.append(initializeCriteria());
    sb.append(initializeListeResultatForRecherche());
    sb.append(initializeDonneeForRecherche());
    sb.append("}\n");
    return sb.toString(); 
  }
  
  private static String initializeCriteria(){
    StringBuilder sb = new StringBuilder();
    sb.append("    // \n");
    sb.append("    // PHASE 1 : CONSTRUCTION DU MODELE INTERNE \n");
    sb.append("    // \n");
    sb.append("    // ===> CRITERES DE RECHERCHE (rechCriteria) \n");
    sb.append("    // (entité mémoire mono-occurrence) \n");
    sb.append("    // \n");
    sb.append("    final BaseEntity<"+name+"CriteriaBean"+"> criteriaEntity = new BaseEntity<"+name+"CriteriaBean"+">( \n");
    sb.append("                                                                                                       CRITERIA_ENTITY_NAME, \n");
    sb.append("                                                                                                       getModel(), \n");
    sb.append("                                                                                                       "+name+"CriteriaBean"+".class); \n");
    sb.append("    criteriaEntity.createEntityController("+name+"CriteriaEntityController"+".class, \n");
    sb.append("                                          StorageStrategy.FULL_MEMORY_BASED); \n");
    sb.append("                                          \n");
    sb.append("    // On passe l'entité en mono-occurrence \n");
    sb.append("    criteriaEntity.setSingleOccurrence(true); \n");
    return sb.toString(); 
  }
  
  private static String initializeDonneeForPanier(){
    StringBuilder sb = new StringBuilder();
    sb.append("    // \n");
    sb.append("    // PHASE 2 : INITIALISATION DES DONNEES \n");
    sb.append("    // \n");
    sb.append("    "+name+"CriteriaBean"+" occurrence = new "+name+"CriteriaBean"+"(); \n");
    sb.append("    \n");        
    sb.append("    criteriaEntity.getController().getOccurrenceGroupStorageModule() \n");
    sb.append("                                  .createSingleOccurrenceGroup(criteriaEntity.getOccurrence(), occurrence, OccurrenceStatus.NEW); \n");
    sb.append("                                  \n");
    return sb.toString();
  }
  
  private static String initializeDonneeForRecherche(){
    StringBuilder sb = new StringBuilder();
    
    sb.append("//\n");
    sb.append("//\n");
    sb.append("// PHASE 3 : INITIALISATION DES DONNEES\n");
    sb.append("//\n");
    sb.append(" // Initialisation de l'entité des critères\n");
    sb.append("entity.getController().getOccurrenceGroupStorageModule().createEmptyOccurrenceGroup(null);\n");
    sb.append("\n");
    sb.append(" _resetCriteria(criteriaEntity);\n");
    sb.append("\n");
    sb.append("final "+name+"CriteriaBean criteria = criteriaEntity.getOccurrence();\n");
    sb.append("getModelObject(criteria).setMonitoringChanges(true);\n");
    sb.append("\n");
    sb.append("//recuperation des infos du profil\n");
    sb.append("ULISUserProfile profil=(ULISUserProfile)FacesUtils.getInstance().resolveUserProfile(FacesContext.getCurrentInstance());\n");
    sb.append("if (profil!=null ){\n");
    sb.append("//  criteria.getTOTIE().setTOTIE_COD(Integer.valueOf(profil.getTOTIE_COD()));\n");
    
    sb.append("}\n");
    sb.append("//recupération des paramètres d'entrés\n");
    sb.append("if (function != null) {\n");
    sb.append("  "+name+"CriteriaBean criteriaBean = criteriaEntity.getOccurrence();\n");
    sb.append("  \n");
    sb.append("  // Recherche directe si on a mis des critères.\n");
    sb.append("  if (getModelObject(criteriaBean).getStatus().isChanged()) {\n");
    sb.append("    getModel().setShowSearchResults(false);\n");
    sb.append("  }\n");
    sb.append("}\n");
    
    return sb.toString();
  }
  
  private static String initializeListeResultatForRecherche(){
    StringBuilder sb = new StringBuilder();
    sb.append("//\n");
    sb.append("//\n");
    sb.append("// ===> RESULTAT DE RECHERCHE (constructeurs)\n");
    sb.append("// (entité basée multi-occurrence)\n");
    sb.append("// on récupère la définition du bloc de résultats\n");
    sb.append("BlocMetierProxy blocResultats = null;\n");
    sb.append("try {\n");
    sb.append("  blocResultats = PersonalizationUtils.getInstance()\n");
    sb.append("                                       .retrievePersonalizedSearchResultBlock(FacesContext.getCurrentInstance());\n");
    sb.append(" }\n");
    sb.append(" catch (final ServletException sEx) {\n");
    sb.append("   final ModelException mex = new ModelException(sEx.getMessage(), sEx);\n");
    sb.append("  if (isLoggingError()) {\n");
    sb.append("    logError(mex.getMessage(), mex);\n");
    sb.append("  }\n");
    sb.append("  throw mex;\n");
    sb.append("}\n");
    sb.append("//\n");
    sb.append("// On crée l'entité de la liste de résultat\n");
    sb.append("final BaseEntity<"+name+"Bean> entity = new BaseEntity<"+name+"Bean>(\n");
    sb.append("  entityName, getModel(),"+name+"Bean.class);\n");
    sb.append(" //\n");
    sb.append("// On paramètre son blockSize par défaut\n");
    sb.append("entity.setDefaultBlockSize(blocResultats.getNombreOccurrencesAffichees().intValue());\n");
    sb.append("//\n");
    sb.append("// On indique que cette entité n'est pas éditable\n");
    sb.append("entity.setEntityEditable(false);\n");
    sb.append("// on recherche le service de recherche associé au bloc de résultat\n");
    sb.append("final String serviceName = blocResultats.getEnsembleDonnees().getComposantAccesDonnees();\n");
    sb.append("//\n");
    sb.append("// On créer le contrôleur d'entité\n");
    sb.append("entity.createEntityController(StorageStrategy.RANDOM_READ_SERVICE_BASED, serviceName,"+name+"SumService.SERVICE_NAME);\n");
    sb.append("\n");
    sb.append("\n");
    sb.append("// On définit les tris possibles\n");
    sb.append("PersonalizationUtils.getInstance().addSorts(blocResultats, entity);\n");
    sb.append("\n");
    return sb.toString();
  }
  
  private static String declareFillShoppingCart(ParametreEntree param){
    StringBuilder sb = new StringBuilder();
    
    sb.append("/**\n");
    sb.append(" * Remplissage du panier avec les constructeurs passés en paramètre de la fonction de navigation\n");
    sb.append(" */\n");
    sb.append("public void fillShoppingCart() {\n");
    sb.append("if (isLoggingDebug())\n"); 
    sb.append("  logDebug(\""+name+suffixe+"ScreenController#fillShoppingCart()\");\n"); 
    sb.append("//\n"); 
    sb.append("// Recherche des paramètres en entrée\n"); 
    sb.append(" "+name+suffixe+"Function functionCall = ("+name+suffixe+"Function) getModel().getCurrentFunctionCall();\n"); 
    sb.append(" if (functionCall != null) {\n"); 
    sb.append("   if (functionCall.getIn"+param.getNom()+"() != null && !functionCall.getIn"+param.getNom()+"().isEmpty()) {\n"); 
    sb.append("    //\n"); 
    sb.append("    // On récupère le panier\n"); 
    sb.append("    OccurrenceGroup<"+name+"Bean> shoppingCart = getShoppingCart();\n"); 
    sb.append("     //\n"); 
    sb.append("    for("+name+"Proxy proxy : functionCall.getIn"+param.getNom()+"()) {\n"); 
    sb.append("      // Ajout du DemandeBean à la liste des demandes sélectionnés\n"); 
    sb.append("      ShoppingCartEntityController<"+name+"Bean> shoppingCartEntityController = (ShoppingCartEntityController<"+name+"Bean>)shoppingCart.getEntity().getController();\n"); 
    sb.append("      shoppingCartEntityController.insertItem(shoppingCart, proxy);\n"); 
    sb.append("     }\n"); 
    sb.append("  }\n"); 
    sb.append(" }\n"); 
    sb.append(" }\n");
    return sb.toString(); 
  }
  
  
  private static String declareProcessValidateForPanier(List<ParametreEntree> listeParam){
    StringBuilder sb = new StringBuilder();
    sb.append("/** \n");
    sb.append(" * Méthode appelée sur la validation du formulaire \n");
    sb.append(" * @param bean action par défaut \n");
    sb.append(" * @return l'outcome indiquant la page suivante \n");
    sb.append(" * @throws ModelException en cas de problème technique \n");
    sb.append(" * @throws CoreBeanException en cas de problème technique \n");
    sb.append(" * @throws NavigationException Exception \n");
    sb.append(" * @throws ServiceException Exception \n");
    sb.append(" */ \n");
    sb.append("public String processValidate(final DefaultAction bean) throws CoreBeanException, \n");
    sb.append("                                                               ModelException, \n");
    sb.append("                                                               NavigationException, \n");
    sb.append("                                                               ServiceException { \n");
    sb.append("  if (isLoggingDebug()) { \n");
    sb.append("    logDebug(\""+name+suffixe+"ScrennController#validate()\"); \n");
    sb.append("  } \n");
    sb.append("  final String result = bean.processDefault(); \n");
    sb.append("  if (DefaultAction.RESULT_SUCCESS.equals(result)) { \n");
    sb.append("    "+name+suffixe+"Function screenFunction = ("+name+suffixe+"Function) getModel().getCurrentFunctionCall(); \n");
    sb.append("    if (null == screenFunction) { \n");
    sb.append("      screenFunction = new "+name+suffixe+"Function(); \n");
    sb.append("    } \n");
    
    for (ParametreEntree param : listeParamEntree) {
      if (param.isSortie() && param.getType().contains("List")){
        sb.append("    final List<"+name+"Bean> selection = getShoppingCart().getCurrentView(); \n");
     // conversion du bean en proxy
        sb.append("List<"+name+"Proxy> proxys = new ArrayList<"+name+"Proxy>(); \n");
        sb.append(name+"Proxy newProxy = null;\n");
        sb.append("for ("+name+"Bean beanPanier : selection) {\n");
        sb.append("  newProxy = new "+name+"Proxy();\n");
        sb.append("  //\n");
        sb.append("  newProxy.setCLE(beanPanier.getCLE());\n");
        sb.append("  proxys.add(newProxy);\n");
        sb.append("}\n");
        sb.append("screenFunction.setOut"+param.getNom()+"(proxys); \n");
      }else{
        //@TODO traitement des autres élément de sortie 
      }
    }
    sb.append("   FacesNavigationConnector.returnFromFunction(); \n");
    sb.append("  } \n");
    sb.append("  \n");
    sb.append("  return null; \n");
    sb.append("} \n");
    return sb.toString(); 
  }
  
  private static String declareInsertAllForPanier(){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * @see sopra.immobilier.model.AbstractShoppingCartScreenController#insertAll()\n");
    sb.append(" */\n");
    sb.append("@Override\n");
    sb.append("public String insertAll() throws ModelException {\n");
    sb.append("  if (isLoggingDebug()) {\n");
    sb.append("    logDebug(\""+name+suffixe+"ScreenController#insertAll()\");\n");
    sb.append("  }\n");
    sb.append("  //\n");
    sb.append("  // On récupère l'entité des lots d'encaissement\n");
    sb.append("  final BaseEntity<"+name+"Bean> lotsEntity = getModel().getBaseEntity(LISTE_RESULTATS_ENTITY_NAME,\n");
    sb.append("                                                                                                   "+name+"Bean.class);\n");
    sb.append("  //\n");
    sb.append("  // On ajoute les éléments\n");
    sb.append("  getShoppingCartController().insertAllItems(getShoppingCart(), lotsEntity.getOccurrences());\n");
    sb.append("  //\n");
    sb.append("  return null;\n");
    sb.append("}//fin insertAll\n");
    return sb.toString(); 
  }
  
  private static String declareSearchForPanier(){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * @see sopra.immobilier.model.AbstractSearchScreenController#search()\n");
    sb.append(" */\n");
    sb.append("@Override\n");
    sb.append("public String search() throws ModelException {\n");
    sb.append("  if (isLoggingDebug()) {\n");
    sb.append("    logDebug(\""+name+suffixe+"ScreenController#search()\");\n");
    sb.append("  }\n");
    sb.append("  //\n");
    sb.append("  // Récupération des critères\n");
    sb.append("  final BaseEntity<"+name+"CriteriaBean> criteriaEntity = getModel().getBaseEntity(CRITERIA_ENTITY_NAME,\n");
    sb.append("                                                                                   "+name+"CriteriaBean.class);\n");
    sb.append("  final "+name+"CriteriaBean criteriaBean = criteriaEntity.getOccurrence();\n");
    sb.append("  // Constitution des critères métier\n");
    sb.append("  ServiceCriteria criteria = ServiceInputOutputFactory.getInstance().createDefaultServiceCriteria();\n");
    sb.append("  //\n");
    sb.append("  // On construit les critères de recherche\n");
    sb.append("  criteria = criteriaEntity.getController().getOccurrenceGroupStorageModule().computeServiceCriteriaFromObject(criteriaBean);\n");
    sb.append("  // Lancement de la recherche\n");
    sb.append("  ((ServiceBasedStorageModule<"+name+"Bean>) getModel().getBaseEntity(LISTE_RESULTATS_ENTITY_NAME,\n");
    sb.append("                                                                                                  "+name+"Bean.class)\n");
    sb.append("                                                                                                  .getController()\n");
    sb.append("                                                                                                  .getOccurrenceGroupStorageModule()).loadDataByCriteria(criteria);\n");
    sb.append("                                                                                                  \n");
    sb.append("  getModel().setShowSearchResults(true);\n");
    sb.append("  //\n");
    sb.append("  return null;\n");
    sb.append("}//fin search\n");
     
    return sb.toString(); 
  }
  
  private static String declareSearchForRecherche(){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * @see sopra.immobilier.model.AbstractSearchScreenController#search()\n");
    sb.append(" */\n");
    sb.append("@Override\n");
    sb.append("public String search() throws ModelException {\n");
    sb.append("if (isLoggingDebug()) {\n");
    sb.append("  logDebug(\""+name+suffixe+"LSTScreenController#search()\");\n");
    sb.append("}\n");
    sb.append("\n");
    sb.append("if (!getModel().isInError()){\n");
    sb.append("  super.search();\n");
    sb.append("  BaseEntity<"+name+"CriteriaBean> criteriaEntity = getCriteriaEntity();\n");
    sb.append("  \n");
    sb.append("  // Récupération des critères\n");
    sb.append("  "+name+"CriteriaBean criteriaBean = criteriaEntity.getOccurrence();\n");
    sb.append("  // Constitution des critères métier\n");
    sb.append("  ServiceCriteria criteria = ServiceInputOutputFactory.getInstance().createDefaultServiceCriteria();\n");
    sb.append("  //\n");
    sb.append("  // On construit les critères de recherche\n");
    sb.append("  criteria = criteriaEntity\n");
    sb.append("  .getController()\n");
    sb.append("  .getOccurrenceGroupStorageModule()\n");
    sb.append("  .computeServiceCriteriaFromObject(criteriaBean);\n");
    sb.append("  // Lancement de la recherche\n");
    sb.append("BaseEntity<"+name+"Bean> proxyEntity = getListeEntity();\n");
    sb.append("((ServiceBasedStorageModule<"+name+"Bean>)proxyEntity\n");
    sb.append("    .getController()\n");
    sb.append("    .getOccurrenceGroupStorageModule()).loadDataByCriteria(criteria);\n");
    sb.append("    \n");
    sb.append("if (!getModel().isInError())\n");
    sb.append(" navigateIfSingleOccurrence(criteriaEntity.getOccurrences()); \n");
    sb.append("}\n");
    sb.append("return null;\n");
    sb.append("}//fin search\n");
     
    return sb.toString(); 
  }
  
  private static String declareSelectForPanier(){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * Méthode d'insertion d'un élément de résultat de la recherche dans le panier.\n");
    sb.append(" * @throws ModelException en cas de problème technique\n");
    sb.append(" * @throws ServiceException Exception\n");
    sb.append(" */\n");
    sb.append("public void select() throws ModelException, ServiceException {\n");
    sb.append("  final FacesContext context = FacesContext.getCurrentInstance();\n");
    sb.append("  final Object bean = FacesUtils.getInstance().getCurrentModelOccurrence(context, Object.class);\n");
    sb.append("  getShoppingCartController().insertItem(getShoppingCart(), bean);\n");
    sb.append("}//fin select\n");
    return sb.toString(); 
  }
  
  private static String declareGetFunctionClass(){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * @see sopra.immobilier.model.ScreenController#getFunctionClass()\n");
    sb.append(" */\n");
    sb.append("@Override\n");
    sb.append("protected Class<? extends AbstractFunctionCall> getFunctionClass() {\n");
    sb.append("  return "+name+suffixe+"Function.class;\n");
    sb.append("}\n");
    return sb.toString();
  }
  
  private static String declareBean(){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * Description :\n");
    sb.append(" * Donnez une description textuelle du service fourni par cette classe.\n");
    sb.append(" *\n");
    sb.append(" * <TABLE>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><B>Attributs SVN :</B></TD>\n");
    sb.append(" *     <TD>&nbsp;</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append("*   <TR>\n");
    sb.append(" *     <TD><I>Nom du fichier :</I></TD>\n");
    sb.append(" *     <TD>$HeadURL:   O:/Kernel5 Livraison/archives/kernel/tools/eclipse/config/ArchitectureImmobilierCodeTemplates.xml-arc  $</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><I>Modifié le :</I></TD>\n");
    sb.append(" *     <TD>$Date:   Apr 16 2009 11:52:40  $</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append(" * </TABLE>\n");
    sb.append(" *\n");
    sb.append(" * @version : $Revision:   1.0  $\n");
    sb.append(" * @author  : SOPRA Group - $Author:   tintin  $\n");
    sb.append(" *\n");
    sb.append(" */\n");
    sb.append(" @ModelBean\n");
    sb.append(" public static class "+name+"Bean extends "+name+"Proxy {\n");
    sb.append("   \n");
    sb.append("   /** Version extraite du gestionnaire de configuration */\n");
    sb.append("   @SuppressWarnings(\"hiding\")\n");
    sb.append("   public static final String CLASS_VERSION = \"$Revision:   1.0  $\";\n");
    sb.append(" }\n");
    return sb.toString();
  }
  
  private static String declareLSTEntityControllerForPanier(){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append("* Description :\n");
    sb.append("* Donnez une description textuelle du service fourni par cette classe.\n");
    sb.append("*\n");
    sb.append("* <TABLE>\n");
    sb.append("*   <TR>\n");
    sb.append("*     <TD><B>Attributs SVN :</B></TD>\n");
    sb.append("*     <TD>&nbsp;</TD>\n");
    sb.append("*   </TR>\n");
    sb.append("*   <TR>\n");
    sb.append("*     <TD><I>Nom du fichier :</I></TD>\n");
    sb.append("*     <TD>$HeadURL:   O:/Kernel5 Livraison/archives/kernel/tools/eclipse/config/ArchitectureImmobilierCodeTemplates.xml-arc  $</TD>\n");
    sb.append("*   </TR>\n");
    sb.append("*   <TR>\n");
    sb.append("*     <TD><I>Modifié le :</I></TD>\n");
    sb.append("*     <TD>$Date:   Apr 16 2009 11:52:40  $</TD>\n");
    sb.append("*   </TR>\n");
    sb.append("* </TABLE>\n");
    sb.append("*\n");
    sb.append("* @version : $Revision:   1.0  $\n");
    sb.append("* @author  : SOPRA Group - $Author:   tintin  $\n");
    sb.append("*\n");
    sb.append("*/\n");
    sb.append("public static class "+name+suffixe+"EntityController extends ShoppingCartEntityController<"+name+"Bean>{\n");
    sb.append(" /** Version extraite du gestionnaire de configuration */\n");
    sb.append(" @SuppressWarnings(\"hiding\")\n");
    sb.append(" public static final String CLASS_VERSION = \"$Revision:   1.0  $\";\n");
    sb.append("}\n");
   return sb.toString();
  }
  
  
  private static String declareCriteriaEntityControllerForRecherche(List<Criteria> dataListCriteria){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * Description :\n");
    sb.append(" * Donnez une description textuelle du service fourni par cette classe.\n");
    sb.append(" *\n");
    sb.append(" * <TABLE>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><B>Attributs SVN :</B></TD>\n");
    sb.append(" *     <TD>&nbsp;</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><I>Nom du fichier :</I></TD>\n");
    sb.append(" *     <TD>$HeadURL:   O:/Kernel5 Livraison/archives/kernel/tools/eclipse/config/ArchitectureImmobilierCodeTemplates.xml-arc  $</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><I>Modifié le :</I></TD>\n");
    sb.append(" *     <TD>$Date:   Apr 16 2009 11:52:40  $</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append(" * </TABLE>\n");
    sb.append(" *\n");
    sb.append(" * @version : $Revision:   1.0  $\n");
    sb.append(" * @author  : SOPRA Group - $Author:   tintin  $\n");
    sb.append(" *\n");
    sb.append(" */\n");
    sb.append(" public static class "+name+"CriteriaEntityController extends AbstractPersonalizationCriteriaEntityController<"+name+"CriteriaBean>{\n");
    sb.append("   /** Version extraite du gestionnaire de configuration */\n");
    sb.append("   @SuppressWarnings(\"hiding\")\n");
    sb.append("  public static final String CLASS_VERSION = \"$Revision:   1.0  $\";\n");
    sb.append("  \n");
    sb.append("  /**\n");
    sb.append("   * @see sopra.immobilier.model.EntityController#adjustPropertiesForServiceParameters(java.util.Map)\n");
    sb.append("   */\n");
    sb.append("  @Override\n");
    sb.append("  protected Map<String, Object> adjustPropertiesForServiceParameters(Map<String, Object> propertyValues) {\n");
    sb.append("   return super.adjustPropertiesForServiceParameters(propertyValues);\n");
    sb.append(" }\n");
    sb.append(" \n");
    sb.append(" /**\n");
    sb.append("  * @see sopra.immobilier.model.EntityController#adjustPropertiesForServiceCriteria(java.util.Map)\n");
    sb.append("  */\n");
    sb.append(" @Override\n");
    sb.append(" protected Map<String, Object>  adjustPropertiesForServiceCriteria (Map<String, Object> propertyValues){\n");
    if (containsComposantMetier(dataListCriteria,"paesi")){
      sb.append("adjustPAESIProperties(propertyValues);\n");
    }
    if (containsComposantMetier(dataListCriteria,"toeso")){
      sb.append("adjustTOESOProperties(propertyValues);\n");
    }
    if (containsComposantMetier(dataListCriteria,"glcon")){
      sb.append("adjustGLCONProperties(propertyValues);\n");
    }
    if (containsComposantMetier(dataListCriteria,"totie")){
      sb.append("adjustTOTIEProperties(propertyValues);\n");
    }
    if (containsComposantMetier(dataListCriteria,"caint")){
      sb.append("adjustTOTIEProperties(propertyValues);\n");
    }
    
    sb.append("   return super.adjustPropertiesForServiceCriteria(propertyValues);\n");
    sb.append(" }\n");
    sb.append(" \n");
    sb.append(" /**\n");
    sb.append("  * Méthode permettant d'entreprendre des traitements sur une occurrence qui\n");
    sb.append("  * vient d'être chargée dans un groupe d'occurrences de l'entité.\n");
    sb.append("  * @param criteres l'élément qui vient d'être chargé\n");
    sb.append("  * @throws ModelException Exception du model\n");
    sb.append("  */\n");
    sb.append(" @Override\n");
    sb.append(" protected void postLoadOccurrence("+name+"CriteriaBean criteres) throws ModelException {   \n");
    sb.append(" }\n");

    
    if (containsComposantMetier(dataListCriteria,"paesi")){
      sb.append(adjustPropertiesPAESIForServiceCriteria());
    }
    if (containsComposantMetier(dataListCriteria,"toeso")){
      sb.append(adjustPropertiesTOESOForServiceCriteria());
    }
    if (containsComposantMetier(dataListCriteria,"glcon")){
      sb.append(adjustPropertiesGLCONForServiceCriteria());
    }
    if (containsComposantMetier(dataListCriteria,"totie")){
      sb.append(adjustPropertiesTOTIEForServiceCriteria());
    }
    if (containsComposantMetier(dataListCriteria,"totie")){
      sb.append(adjustPropertiesCAINTForServiceCriteria());
    }
    sb.append(" \n");
    sb.append("}\n"); 
   return sb.toString();
}
  
  private static String declareCriteriaEntityControllerForPanier(List<Criteria> dataListCriteria){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * Description :\n");
    sb.append(" * Donnez une description textuelle du service fourni par cette classe.\n");
    sb.append(" *\n");
    sb.append(" * <TABLE>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><B>Attributs SVN :</B></TD>\n");
    sb.append(" *     <TD>&nbsp;</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><I>Nom du fichier :</I></TD>\n");
    sb.append(" *     <TD>$HeadURL:   O:/Kernel5 Livraison/archives/kernel/tools/eclipse/config/ArchitectureImmobilierCodeTemplates.xml-arc  $</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append(" *   <TR>\n");
    sb.append(" *     <TD><I>Modifié le :</I></TD>\n");
    sb.append(" *     <TD>$Date:   Apr 16 2009 11:52:40  $</TD>\n");
    sb.append(" *   </TR>\n");
    sb.append(" * </TABLE>\n");
    sb.append(" *\n");
    sb.append(" * @version : $Revision:   1.0  $\n");
    sb.append(" * @author  : SOPRA Group - $Author:   tintin  $\n");
    sb.append(" *\n");
    sb.append(" */\n");
    sb.append(" public static class "+name+"CriteriaEntityController extends ShoppingCartEntityController<"+name+"CriteriaBean>{\n");
    sb.append("   /** Version extraite du gestionnaire de configuration */\n");
    sb.append("   @SuppressWarnings(\"hiding\")\n");
    sb.append("  public static final String CLASS_VERSION = \"$Revision:   1.0  $\";\n");
    sb.append("  \n");
    sb.append("  /**\n");
    sb.append("   * @see sopra.immobilier.model.EntityController#adjustPropertiesForServiceParameters(java.util.Map)\n");
    sb.append("   */\n");
    sb.append("  @Override\n");
    sb.append("  protected Map<String, Object> adjustPropertiesForServiceParameters(Map<String, Object> propertyValues) {\n");
    sb.append("   return super.adjustPropertiesForServiceParameters(propertyValues);\n");
    sb.append(" }\n");
    sb.append(" \n");
    sb.append(" /**\n");
    sb.append("  * @see sopra.immobilier.model.EntityController#adjustPropertiesForServiceCriteria(java.util.Map)\n");
    sb.append("  */\n");
    sb.append(" @Override\n");
    sb.append(" protected Map<String, Object>  adjustPropertiesForServiceCriteria (Map<String, Object> propertyValues){\n");
    if (containsComposantMetier(dataListCriteria,"paesi")){
      sb.append("adjustPAESIProperties(propertyValues);\n");
    }
    if (containsComposantMetier(dataListCriteria,"toeso")){
      sb.append("adjustTOESOProperties(propertyValues);\n");
    }
    
    sb.append("   return super.adjustPropertiesForServiceCriteria(propertyValues);\n");
    sb.append(" }\n");
    sb.append(" \n");
    sb.append(" /**\n");
    sb.append("  * Méthode permettant d'entreprendre des traitements sur une occurrence qui\n");
    sb.append("  * vient d'être chargée dans un groupe d'occurrences de l'entité.\n");
    sb.append("  * @param criteres l'élément qui vient d'être chargé\n");
    sb.append("  * @throws ModelException Exception du model\n");
    sb.append("  */\n");
    sb.append(" @Override\n");
    sb.append(" protected void postLoadOccurrence("+name+"CriteriaBean criteres) throws ModelException {   \n");
    sb.append(" }\n");

    
    if (containsComposantMetier(dataListCriteria,"paesi")){
      sb.append(adjustPropertiesPAESIForServiceCriteria());
    }
    if (containsComposantMetier(dataListCriteria,"toeso")){
      sb.append(adjustPropertiesTOESOForServiceCriteria());
    }
    sb.append(" \n");
    sb.append("}\n");
   return sb.toString();
}
  private static String adjustPropertiesPAESIForServiceCriteria(){
    StringBuilder sb = new StringBuilder();
    sb.append(" /**\n");
    sb.append(" * @param propertyValues valeurs des propriétés\n");
    sb.append("*/\n");
    sb.append("private void adjustPAESIProperties(Map<String, Object> propertyValues){\n");
    sb.append("PAESICriterionBean PAESI = (PAESICriterionBean)propertyValues.get("+name+"ServiceCriteria.PAESI_CRITERION_NAME);\n");
    sb.append("if (PAESI != null){\n");
    sb.append("   propertyValues.put(\"PAESI_CODEXT\", PAESI.getPAESI_CODEXT());\n");
    sb.append("  propertyValues.put(\"PAESI_NUM\", PAESI.getPAESI_NUM());\n");
    sb.append("   propertyValues.put(\"PAESI_LIB\", PAESI.getPAESI_LIB());\n");
    sb.append("  //propertyValues.put(\"INDPAESIInferieur\", PAESI.getINDPAESIInferieur());\n");
    // @TODO Si plusieur PAESI dans la meme page utiliser l'attribut NomCritriaName
    sb.append("   propertyValues.remove("+name+"ServiceCriteria.PAESI_CRITERION_NAME);\n");
    
    sb.append("  }\n");
    sb.append("  }\n");
  return sb.toString();
  }
  
  private static String adjustPropertiesTOESOForServiceCriteria(){
    StringBuilder sb = new StringBuilder();
    sb.append(" /**\n");
    sb.append(" * @param propertyValues valeurs des propriétés\n");
    sb.append("*/\n");
    sb.append("private void adjustTOESOProperties(Map<String, Object> propertyValues){\n");
    sb.append("TOESOCriterion toesoCriterion = (TOESOCriterion) propertyValues.get("+name+"ServiceCriteria.TOESO_CRITERION_NAME);\n");
    sb.append("if(toesoCriterion != null) {\n");
    sb.append("  propertyValues.put("+name+"ServiceCriteria.TOESO_COD_CRITERION_NAME,\n");
    sb.append("                     toesoCriterion.getTOESO_COD());\n");
    sb.append("  // on supprime le critère complexe de la map de valeur.\n");
   // @TODO Si plusieur PAESI dans la meme page utiliser l'attribut NomCritriaName
    sb.append("   propertyValues.remove("+name+"ServiceCriteria.TOESO_CRITERION_NAME);\n");
    
    sb.append("  }\n");
    sb.append("  }\n");
  return sb.toString();
  }
  
  private static String adjustPropertiesGLCONForServiceCriteria(){
    StringBuilder sb = new StringBuilder();
    sb.append(" /**\n");
    sb.append(" * @param propertyValues valeurs des propriétés\n");
    sb.append("*/\n");
    sb.append("private void adjustGLCONProperties(Map<String, Object> propertyValues){\n");
    sb.append("GLCONCriterion glconCriterion = (GLCONCriterion) propertyValues.get("+name+"ServiceCriteria.GLCON_CRITERION_NAME);\n");
    sb.append("if(glconCriterion != null) {\n");
    sb.append("  propertyValues.put("+name+"ServiceCriteria.GLCON_NUM_CRITERION_NAME,\n");
    sb.append("                     glconCriterion.getGLCON_NUM());\n");
    sb.append("  propertyValues.put("+name+"ServiceCriteria.GLCON_NUMVER_CRITERION_NAME,\n");
    sb.append("                     glconCriterion.getGLCON_NUMVER());\n");
    sb.append("  // on supprime le critère complexe de la map de valeur.\n");
   // @TODO Si plusieur PAESI dans la meme page utiliser l'attribut NomCritriaName
    sb.append("   propertyValues.remove("+name+"ServiceCriteria.GLCON_CRITERION_NAME);\n");
    
    sb.append("  }\n");
    sb.append("  }\n");
  return sb.toString();
  }
  
  private static String adjustPropertiesTOTIEForServiceCriteria(){
    StringBuilder sb = new StringBuilder();
    sb.append(" /**\n");
    sb.append(" * @param propertyValues valeurs des propriétés\n");
    sb.append("*/\n");
    sb.append("private void adjustTOTIEProperties(Map<String, Object> propertyValues){\n");
    sb.append("TOTIECriterion totieCriterion = (TOTIECriterion) propertyValues.get("+name+"ServiceCriteria.TOTIE_CRITERION_NAME);\n");
    sb.append("if(totieCriterion != null) {\n");
    sb.append("  propertyValues.put("+name+"ServiceCriteria.TOTIE_COD_CRITERION_NAME,\n");
    sb.append("                     totieCriterion.getTOTIE_COD());\n");
    sb.append("  propertyValues.put("+name+"ServiceCriteria.TOTIE_LIB_CRITERION_NAME,\n");
    sb.append("                     totieCriterion.getTOTIE_LIB());\n");
    sb.append("  // on supprime le critère complexe de la map de valeur.\n");
   // @TODO Si plusieur PAESI dans la meme page utiliser l'attribut NomCritriaName
    sb.append("   propertyValues.remove("+name+"ServiceCriteria.TOTIE_CRITERION_NAME);\n");
    
    sb.append("  }\n");
    sb.append("  }\n");
  return sb.toString();
  }
  
  private static String adjustPropertiesCAINTForServiceCriteria(){
    StringBuilder sb = new StringBuilder();
    sb.append(" /**\n");
    sb.append(" * @param propertyValues valeurs des propriétés\n");
    sb.append("*/\n");
    sb.append("private void adjustCAINTProperties(Map<String, Object> propertyValues){\n");
    sb.append("CAINTCriterion caintCriterion = (CAINTCriterion) propertyValues.get("+name+"ServiceCriteria.CAINT_CRITERION_NAME);\n");
    sb.append("if (caintCriterion != null) {\n");
    sb.append("  propertyValues.put("+name+"ServiceCriteria.CAINT_NUM_CRITERION_NAME, caintCriterion.getCAINT_NUM());\n");
    sb.append("  propertyValues.put("+name+"ServiceCriteria.CAINT_LIB_CRITERION_NAME, caintCriterion.getCAINT_LIB());\n");
    sb.append("  propertyValues.remove("+name+"ServiceCriteria.CAINT_CRITERION_NAME);      \n");
   
    sb.append("  }\n");
    sb.append("  }\n");
  return sb.toString();
  }
  
  private static String selectForPanier(){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * Action de sélection d'un élément au sein de la liste\n");
    sb.append(" * \n");
    sb.append(" * @return le nom logique de la vue suivante\n");
    sb.append(" * @throws NavigationException en cas d'erreur de navigation\n");
    sb.append(" */\n");
    sb.append("public String select() throws NavigationException {\n");
    sb.append("  if (isLoggingDebug()) {\n");
    sb.append("    logDebug(\""+name+suffixe+"ScreenController#select()\");\n");
    sb.append("  }\n");
    sb.append("  //\n");
    sb.append("  final "+name+"Bean occurence = getSelectedOccurrence();\n");
    sb.append("  Assert.notNull(\"selected\", occurence);\n");
    sb.append("  \n");
    sb.append("  "+name+suffixe+"Function functionCall = ("+name+suffixe+"Function) getModel().getCurrentFunctionCall();\n");
    sb.append("  if(functionCall != null){\n");
    sb.append("    // Si on est en mode de sélection, on revient sur la page appelante\n");
    sb.append("    if (null != functionCall.getInSELECTION() && functionCall.getInSELECTION().booleanValue()) {\n");
    sb.append("      return \"success\";\n");
    sb.append("    }\n");
    sb.append("  }\n");
    sb.append("  // Si on est en appel de fonction, on renseigne les informations sur l'élément sélectionné\n");
    sb.append("  \n");
    sb.append(" //@TODO APPEL de La SYNTHESE \n");
    sb.append(" /* \n");
    sb.append(" NOM_FONCTION_SYNTHESE_SYNFunction synFunction = new NOM_FONCTION_SYNTHESE_SYNFunction();\n");
    sb.append("  if (occurence.getCHAMPS() != null) {\n");
    sb.append("    synFunction.setInCHAMP(occurence.getCHAMP());\n");
    sb.append("  }\n");
    sb.append(" */ \n");
    
    sb.append("  \n");
    sb.append("  FacesNavigationConnector.invoke(synFunction, null);\n");
    sb.append("  \n");
    sb.append("  return null;\n");
    sb.append("}\n");
  return sb.toString();
  }
  
  private static String selectForRecherche(){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * Action de sélection d'un élément au sein de la liste\n");
    sb.append(" * \n");
    sb.append(" * @return le nom logique de la vue suivante\n");
    sb.append(" * @throws NavigationException en cas d'erreur de navigation\n");
    sb.append(" */\n");
    sb.append("public String select() throws NavigationException {\n");
    sb.append("  if (isLoggingDebug()) {\n");
    sb.append("    logDebug(\""+name+suffixe+"ScreenController#select()\");\n");
    sb.append("  }\n");
    sb.append("  //\n");
    sb.append("  final "+name+"Bean occurence = getSelectedOccurrence();\n");
    sb.append("  Assert.notNull(\"selected\", occurence);\n");
    sb.append("  \n");
    sb.append("  "+name+suffixe+"Function functionCall = ("+name+suffixe+"Function) getModel().getCurrentFunctionCall();\n");
    sb.append("  if(functionCall != null){\n");
    sb.append("    // Si on est en mode de sélection, on revient sur la page appelante\n");
    sb.append("    if (null != functionCall.getInSELECTION() && functionCall.getInSELECTION().booleanValue()) {\n");
    sb.append("      return \"success\";\n");
    sb.append("    }\n");
    sb.append("  }\n");
    sb.append("  // Si on est en appel de fonction, on renseigne les informations sur l'élément sélectionné\n");
    sb.append("  \n");
    sb.append(" //@TODO APPEL de La SYNTHESE \n");
    sb.append(" /* \n");
    sb.append(" NOM_FONCTION_SYNTHESE_SYNFunction synFunction = new NOM_FONCTION_SYNTHESE_SYNFunction();\n");
    sb.append("  if (occurence.getCHAMPS() != null) {\n");
    sb.append("    synFunction.setInCHAMP(occurence.getCHAMP());\n");
    sb.append("  }\n");
    sb.append(" */ \n");
    
    sb.append("  \n");
    sb.append("  FacesNavigationConnector.invoke(synFunction, null);\n");
    sb.append("  \n");
    sb.append("  return null;\n");
    sb.append("}\n");
  return sb.toString();
  }
  
  private static String getSynthesisCallForRecherche(){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * @see sopra.immobilier.model.ULIS.AbstractInOutSearchScreenController#getSynthesisFunctionCall(java.lang.Object)\n");
    sb.append(" */\n");
    sb.append("@Override\n");
    sb.append("protected AbstractFunctionCall getSynthesisFunctionCall("+name+"Bean occurenceSelected) {\n");
    sb.append("  final "+name+suffixe+"Function function = ("+name+suffixe+"Function) getModel().getCurrentFunctionCall();\n");
    sb.append("  SyntheseFunction syntheseFunction = new SyntheseFunction();\n");
    sb.append("  //syntheseFunction.setInGLCON_NUM(occurenceSelected.getGLCON_NUM());\n");
    sb.append("  return syntheseFunction;\n");
    sb.append(" }\n");
  return sb.toString();
  }
  
  
  
  private static String processReset(){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * @see sopra.immobilier.model.AbstractSearchScreenController#processReset(DefaultAction action)\n");
    sb.append(" */\n");
    sb.append(" @Override\n");
    sb.append(" public String processReset(final DefaultAction action) throws ModelException {\n");
    sb.append("  if (isLoggingDebug()) {\n");
    sb.append("    logDebug(\""+name+suffixe+"ScreenController#reset()\");\n");
    sb.append("  }\n");
    sb.append("  //\n");
    sb.append("   action.processDefault();\n");
    sb.append("   //\n");
    sb.append("   super.processReset(action);\n");
    sb.append("   //\n");
    sb.append("  // Réinitialisation des critères\n");
    sb.append("  final BaseEntity<"+name+"CriteriaBean> criteriaEntity = getCriteriaEntity();\n");
    sb.append("  _resetCriteria(criteriaEntity);\n");
    sb.append("  //\n");
    sb.append("  getModel().setShowSearchResults(false);\n");
    sb.append("  return null;\n");
    sb.append(" }\n");
    return sb.toString();
  }
  
  private static String _resetCriteria(List<Criteria> listeCriteria){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * Réinitialise les critères de recherche\n");
    sb.append(" * \n");
    sb.append(" * @param criteriaEntity l'entité contenant les critères de recherche\n");
    sb.append(" * @throws ModelException erreur technique survient lors d'un traitement dans le modèle interne\n");
    sb.append(" */\n");
    sb.append(" private void _resetCriteria(final BaseEntity<"+name+"CriteriaBean> criteriaEntity)\n");
    sb.append("                                                                                            throws ModelException {\n");
    sb.append("   \n");
    sb.append("   \n");
    sb.append(" // Alimentation initiale des entités mémoire\n");
    sb.append("  ((MemoryBasedStorageModule<"+name+"CriteriaBean>) criteriaEntity.getController()\n");
    sb.append("                                                                            .getOccurrenceGroupStorageModule()).createSingleOccurrenceGroup(null,\n");
    sb.append("                                                                                                                                            null,\n");
    sb.append("                                                                                                                                            OccurrenceStatus.INITED);\n");
    sb.append("                                                                                                                                            \n");
    sb.append("  final "+name+"CriteriaBean criteria = criteriaEntity.getOccurrence();\n");
    sb.append("  getModelObject(criteria).setMonitoringChanges(false);\n");
    sb.append("  \n");
    
    
    //@TODO traiter les PAESI TOESO TOTIE...
    for (Criteria critere : listeCriteria) {
      if (critere.getType().equalsIgnoreCase("Boolean") && critere.isCocheeParDefaut()){
        sb.append("  criteria.set"+critere.getNom()+"("+critere.isCocheeParDefaut()+");\n");
      }else if (critere.getComposantMetier()!=null && !critere.getComposantMetier().getType().isEmpty()){
        sb.append("  criteria.set"+critere.getComposantMetier().getType().toUpperCase()+"(new "+critere.getComposantMetier().getType().toUpperCase()+"Criterion());\n");
      }
      else if (critere.getType().equalsIgnoreCase("Date") && critere.isDateInitialisee()){
        sb.append("  criteria.set"+critere.getNom()+"(DateTools.getInstance().getToday());\n");
      }
      else{
        sb.append("  criteria.set"+critere.getNom()+"(null);\n");
      }
      
    }
    
    sb.append("  getModelObject(criteria).setMonitoringChanges(true);\n");
    sb.append("}\n");
    return sb.toString();
  }
  
  private static String processCancel(){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * Méthode appelée sur l'annulation de la transaction courante\n");
    sb.append(" * @param defaultAction DefaultAction\n");
    sb.append(" * @return l'outcome indiquant la page suivante\n");
    sb.append(" */\n");
    sb.append("public String processCancel(DefaultAction defaultAction) {\n");
    sb.append("  if (isLoggingDebug())\n");
    sb.append("    logDebug(\""+name+suffixe+"ScreenController#cancel()\");\n");
    sb.append("   //\n");
    sb.append("     \n");
    sb.append("   return \"cancel\";\n");
    sb.append(" }\n");
    return sb.toString();
  }
    
  private static boolean contains(List<ParametreEntree> dataList, String type){
    boolean found = false;
    Iterator<ParametreEntree> it = dataList.iterator();
    while (it.hasNext()&&(!found)) {
      ParametreEntree dataTypeMandatory = it.next();
      found = dataTypeMandatory.getType().equalsIgnoreCase(type);
    }
    return found;
    
  }
  
  private static boolean containsComposantMetier(List<Criteria> dataList, String type){
    boolean found = false;
    Iterator<Criteria> it = dataList.iterator();
    while (it.hasNext()&&(!found)) {
      Criteria critere = it.next();
      TypeComposantMetier composant = critere.getComposantMetier();
      if (composant!=null){
        found = critere.getComposantMetier().getType().equalsIgnoreCase(type);
      }
    }
    return found;
  }

  
}
