
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

public class ScreenControllerGestionGenerator {
  public static String packageName="GL"; //exemple : GL
  public static String name="GLTAP607_PanierGroupeDePrix"; //Exemple : GLTAP606_PanierRubriquesTarification
  public static String suffixe="LST"; //Exemple : LST (GLTAP606_PanierRubriquesTarificationLST)
  public static String pathOutput = "D:\\Ulis\\generateur\\BaseServiceGenerator\\src\\";//"D:\\projects\\workspace-ulisng\\maven.1285856976189\\src\\main\\java\\sopra\\immobilier\\business\\ULIS\\referentiel\\";
  public static String pathInput = "D:\\Ulis\\generateur\\BaseServiceGenerator\\src\\";
  public static List<ParametreEntree> listeParamEntree =null;
  public static List<Proxy> listeProxy =null;
  public static List<Criteria> listeCriteria =null;
  
  public ScreenControllerGestionGenerator(List<ParametreEntree> paramEntree,List<Criteria> paramCriteria,List<Proxy> paramProxy,String pathOutput, String packageName,String name,String suffixe){
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
  
  
  
  public static void generateScreenControllerClassForGestion(){
    
    try {
      File file = new File(pathOutput+name+suffixe+"ScreenController.java");
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
      out.append(declarePackage(packageName));
      out.append(declareImportClassForRecherche(listeParamEntree));
      out.append(declareClassCommentary());
      out.append(declareClassForGestion());
      out.append(declareClassConstructeurForRecherche());
      
      out.append(declareProcessInitialize(listeParamEntree, listeProxy));
      out.append(declareProcessContinue());
      out.append(declareprocessStore());
      
      //out.append(selectForRecherche());
      /*out.append(getSynthesisCallForRecherche());
    out.append(declareSearchForRecherche());
    out.append(processCancel());*/
      
      out.append(declareGetFunctionClass());
      
      
      generateEntityControllerClass();
      
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
  
  private static String declareClassForGestion() {
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
    sb.append("  public static final String MERE_ENTITY_NAME = \"mereEntity\";\n");
    
    sb.append("/** Nom de l'entité constructeurs */\n");
    sb.append("private  String entityName = null;\n");

    
    sb.append("/**\n");
    sb.append(" * Le contrôleur de l'entité des critères \n");
    sb.append(" * \n");
    sb.append(" * @return le contrôleur demandé\n");
    sb.append(" * @throws ModelException erreur technique survient lors d'un traitement dans le modèle interne\n");
    sb.append(" */\n");
    sb.append(" protected BaseEntity<"+name+"Bean> getMereEntity() throws ModelException {\n");
    sb.append("   return getModel().getBaseEntity(MERE_ENTITY_NAME, "+name+"Bean.class);\n");
    sb.append(" }\n");
    sb.append(" \n");
    
    
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
    sb.append("   super();\n");
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
  
  private static String declareProcessInitialize(List<ParametreEntree> paramEntree,List<Proxy> listeProxy){
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
    
    sb.append(initialize());
    sb.append(initializeDonnee());

    sb.append("}\n");
    return sb.toString(); 
  }
  
  private static String initialize(){
    StringBuilder sb = new StringBuilder();
    sb.append("    // \n");
    sb.append("    // PHASE 1 : CONSTRUCTION DU MODELE INTERNE \n");
    sb.append("    // \n");
    
    if (!isMulti()){
    sb.append("// Single-occurrence\n"); 
    sb.append("BaseEntity<"+name+"Bean> entity = new BaseEntity<"+name+"Bean>(MERE_ENTITY_NAME , getModel(), "+name+"Bean.class);\n");
    sb.append("// On définit l'entité comme étant Single-occurrence\n");
    sb.append("entity.setSingleOccurrence(true);\n");
    sb.append("entity.setEntityEditable(true);\n");
    sb.append("entity.createEntityController("+name+"EntityController.class,StorageStrategy.FULL_SERVICE_BASED,"+name+"Service.SERVICE_NAME);\n");
    sb.append("\n");
    }else{
      sb.append("// Multi-occurrence\n"); 
      sb.append("BaseEntity<"+name+"Bean> entity = new BaseEntity<"+name+"Bean>(MERE_ENTITY_NAME , getModel(), "+name+"Bean.class);\n");
      sb.append("// On définit l'entité comme étant Multi-occurrence\n");
      sb.append("entity.setSingleOccurrence(false);\n");
      sb.append("entity.setDefaultBlockSize(2);\n");
      sb.append("entity.setPaginationLockingMode(PaginationLockingMode.FREE_PAGINATION);\n");
      sb.append("entity.setEntityEditable(true);\n");
      sb.append("entity.createEntityController(TA_IntervenantsEntityController.class,StorageStrategy.FULL_SERVICE_BASED,TA_IntervenantsService.SERVICE_NAME);\n");
      sb.append("entity.setInitializedOccurrencesIncludedInBusinessView(false);\n");
    }
    
    		
    return sb.toString(); 
  }
  

  
  private static String initializeDonnee(){
    StringBuilder sb = new StringBuilder();
    
    sb.append("// \n");
    sb.append("// PHASE 2 : INITIALISATION DES DONNEES\n");
    sb.append("// \n");
    sb.append("\n");
    sb.append("\n");
    sb.append("ServiceBasedStorageModule<"+name+"Bean> storageModule = entity.getController().getServiceBasedStorageModule();\n");
    sb.append("OccurrenceGroup<"+name+"Bean> occurrenceGroup = storageModule.createEmptyOccurrenceGroup(null);\n");
    
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
  
  
  private static String declareProcessContinue(){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append("* Méthode appelée sur la validation du formulaire\n");
    sb.append("* \n");
    sb.append("* @param defaultAction l'action par défaut\n");
    sb.append("* @return l'outcome indiquant la page suivante\n");
    sb.append("* @throws ModelException en cas de problème technique\n");
    sb.append("* @throws CoreBeanException en cas de problème technique\n");
    sb.append("* @throws NavigationException en cas de problème technique\n");
    sb.append("* @throws ServiceException en cas de problème technique\n");
    sb.append("*/\n");
    sb.append("public String processContinue(DefaultAction defaultAction) \n");
    sb.append("                            throws CoreBeanException, \n");
    sb.append("                            ModelException, \n");
    sb.append("                            NavigationException, ServiceException {     \n");
    sb.append(" \n");
    sb.append("//@TODO\n");
    sb.append("return null;\n");
    sb.append("}\n");
  return sb.toString();
  }
  
  private static String declareprocessStore(){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append("* Méthode appelée sur l'enregistrement du formulaire\n");
    sb.append("* \n");
    sb.append("* @param defaultAction l'action par défaut\n");
    sb.append("* @return l'outcome indiquant la page suivante\n");
    sb.append("* @throws ModelException en cas de problème technique\n");
    sb.append("* @throws NavigationException en cas de problème technique\n");
    sb.append("* @throws ServiceException en cas de problème technique\n");
    sb.append("*/\n");
    sb.append("public String processStore(DefaultAction defaultAction) \n");
    sb.append("                         throws ModelException, \n");
    sb.append("                          NavigationException, ServiceException {\n");
    
    sb.append("//@TODO\n");
    sb.append("return null;\n");
    sb.append("}\n");
  return sb.toString();
  }
  
  private static void generateEntityControllerClass() {
    try {
      File file = new File(pathOutput+name+"EntityController.java");
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
      out.append(declarePackage(packageName));
      out.append(declareImportClassForRecherche(listeParamEntree));
      out.append(declareEntityControllerForGestion(listeCriteria));
      out.append(declareEntityControllerAdjust(listeCriteria));
      out.append(declareEntityControllerOnValueChange(listeCriteria));
      
      out.append("}//fin classe ").append(name).append("EntityController");
      out.close();
    } catch (Exception e) {
      System.out.println("erreur lors de l'eccriture du fichier EntityController");
    } 
  }
  
  
  
  private static String declareEntityControllerForGestion(List<Criteria> dataListCriteria){
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
    sb.append(" public class "+name+"EntityController extends AbstractPersonalizationCriteriaEntityController<"+name+"Bean>{\n");
    sb.append("   /** Version extraite du gestionnaire de configuration */\n");
    sb.append("   @SuppressWarnings(\"hiding\")\n");
    sb.append("  public static final String CLASS_VERSION = \"$Revision:   1.0  $\";\n");
    
    return sb.toString();
  }
  
  private static String declareEntityControllerOnValueChange(List<Criteria> dataListCriteria){
    StringBuilder sb = new StringBuilder();
    sb.append("/**\n");
    sb.append(" * @see sopra.immobilier.model.EntityController#onValueChange(java.lang.Object, java.lang.String, boolean, java.lang.Object, java.lang.Object)\n");
    sb.append(" */\n");
    sb.append("@Override\n");
    sb.append(" protected void onValueChange("+name+"Bean element, String propertyName, boolean notifiedByChild,\n");
    sb.append("                             Object oldValue, Object newValue) throws ModelException {\n");
    sb.append("  \n");
    sb.append("  ModelUtils.getInstance().resetErrorsForProperty(element, propertyName);\n");
    
    
    for (Criteria critere: dataListCriteria){
      if (critere.getControle()!=null){
        sb.append("if (\""+critere.getNom()+"\".equals(propertyName)){\n");
        if (critere.getControle().getCodeErreur()!=null){
          sb.append("boolean result=false;\n");
          sb.append("if (!result){\n");
          sb.append("ModelUtils.getInstance().getModelObject(element).addError(new ModelError(\""+packageName+"."+name+".err"+critere.getControle().getCodeErreur()+"\", \""+critere.getNom()+"\", ModelErrorSeverity.ERROR_SEVERITY_LEVEL));\n");
          sb.append("} \n");
        }
        sb.append("}\n");
      }
    }
    sb.append(" }\n");
    return sb.toString();
  }
  private static String declareEntityControllerAdjust(List<Criteria> dataListCriteria){
    StringBuilder sb = new StringBuilder();
    
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
    sb.append(" protected void postLoadOccurrence("+name+"Bean criteres) throws ModelException {   \n");
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
  
  private static boolean isMulti(){
    for (Criteria criteria : listeCriteria) {
      if (criteria.getMulti()==true){
        return true;
      }
    }
    return false;
  }
}
