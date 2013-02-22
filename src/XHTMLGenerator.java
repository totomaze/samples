import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Paquet de définition
 */

/**
 * Description :
 * Donnez une description textuelle du service fourni par cette classe.
 *
 * <TABLE>
 *   <TR>
 *     <TD><B>Attributs SVN :</B></TD>
 *     <TD>&nbsp;</TD>
 *   </TR>
 *   <TR>
 *     <TD><I>Nom du fichier :</I></TD>
 *     <TD>$HeadURL:   O:/Kernel5 Livraison/archives/kernel/tools/eclipse/config/ArchitectureImmobilierCodeTemplates.xml-arc  $</TD>
 *   </TR>
 *   <TR>
 *     <TD><I>Modifié le :</I></TD>
 *     <TD>$Date:   Apr 16 2009 11:52:40  $</TD>
 *   </TR>
 * </TABLE>
 *
 * @version : $Revision:   1.0  $
 * @author  : SOPRA Group - $Author:   tintin  $
 *
 */
public class XHTMLGenerator {
  public static String packageName="CX"; //exemple : GL
  public static String name="CXFAY009_IdDossierFondsAide"; //Exemple : GLTAP606_PanierRubriquesTarification
  public static String suffixe="SYN"; //Exemple : LST (GLTAP606_PanierRubriquesTarificationLST)
  public static String pathOutput = "D:\\Documents and Settings\\gThomazeau\\Mis documentos\\copyPaste\\afaire\\";//"D:\\projects\\workspace-ulisng\\maven.1285856976189\\src\\main\\java\\sopra\\immobilier\\business\\ULIS\\referentiel\\";
  public static String pathInput = "D:\\Documents and Settings\\gThomazeau\\Mis documentos\\copyPaste\\afaire\\";
  public static List<Proxy> listeProxy =null;
  public static List<Criteria> listeCriteria =null;
  /** Version extraite du gestionnaire de configuration */
  public static final String CLASS_VERSION = "$Revision:   1.0  $";
  
  //privée
  private static List<TypeBloc> listeBloc=null;
  private static int indexBloc=0;
  
  public XHTMLGenerator(String pathInput,String pathOutput, String packageName,String name,String suffixe,List<Proxy> listeProxy,List<Criteria> listeCriteria){
    this.packageName=packageName;
    this.pathOutput=pathOutput;
    this.pathInput=pathInput;
    this.name=name;
    this.suffixe=suffixe;
    this.listeProxy= listeProxy;
    this.listeCriteria= listeCriteria;
  }
  
  public static void main(String[] argv) throws Exception {
    generateXHTMLForPanier();
  }
  
  public static void generateXHTMLForPanier() throws Exception {
    //Getting table column, type and mandatory flag
    extractionBloc(listeCriteria);
    generateServiceClassForPanier(pathOutput, listeCriteria, listeProxy);
    generatePropertie(pathOutput,listeCriteria, listeProxy);
  }
  
  public static void generateXHTMLForRecherche() throws Exception {
    //Getting table column, type and mandatory flag
    extractionBloc(listeCriteria);
    generateServiceClassForRecherche(pathOutput, listeCriteria, listeProxy);
    generatePropertie(pathOutput,listeCriteria, listeProxy);
  }
  
 
  
  public static void generateXHTMLForGestion() throws Exception {
    //Getting table column, type and mandatory flag
    extractionBloc(listeCriteria);
    generateClassForGestion(pathOutput, listeCriteria, listeProxy);
    generatePropertie(pathOutput,listeCriteria, listeProxy);
  }
  
  public static void generateServiceClassForPanier(String path, 
                                           List<Criteria> dataList,List<Proxy> dataListProxy) {
    try {
      File file = new File(path+name+suffixe+".xhtml");
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
      out.append(declareEnteteFichier());
      out.append(declareEnteteForPanier());
      out.append(declareListeResultatForPanier(dataListProxy, false, false));
      out.append(declareListePanier(dataListProxy));
      out.append(declareBloc(dataList));
      out.append(declarePiedPage());
      out.close();
    } catch (Exception e) {
      //Exception non soulevée
    } 
    
  }
  
  public static void generateServiceClassForRecherche(String path, 
                                          List<Criteria> dataList,List<Proxy> dataListProxy) {
   try {
     File file = new File(path+name+suffixe+".xhtml");
     BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
     out.append(declareEnteteFichier());
     out.append(declareEnteteForRecherche());
     out.append(declareListeResultatForRecherche());
     out.append(declareBloc(dataList));
     out.append(declarePiedPage());
     out.close();
   } catch (Exception e) {
     System.out.println(e.getMessage());
     System.out.println(e.getStackTrace().toString());
   } 
   
 }
  

  public static void generateClassForGestion(String path, 
                                                      List<Criteria> dataList,List<Proxy> dataListProxy) {
               try {
                 File file = new File(path+name+suffixe+".xhtml");
                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
                 out.append(declareEnteteFichier());
                 out.append(declareEnteteForGestion());
                 
                 out.append(declareBlocForGestion(dataList));
                 out.append(declarePiedPage());
                 out.close();
               } catch (Exception e) {
                 System.out.println(e.getMessage());
                 System.out.println(e.getStackTrace().toString());
               } 
               
             }
  
  private static String declareEnteteFichier(){
    StringBuilder sb = new StringBuilder();
    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \n");
    sb.append("                      \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n");
    sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" \n");
    sb.append("      xmlns:h=\"http://java.sun.com/jsf/html\"\n");
    sb.append("      xmlns:f=\"http://java.sun.com/jsf/core\"\n");
    sb.append("      xmlns:ui=\"http://java.sun.com/jsf/facelets\"\n");
    sb.append("      xmlns:ih=\"http://immobilier.sopragroup.com/faces/html\"\n");
    sb.append("       xmlns:if=\"http://immobilier.sopragroup.com/faces/core\"\n");
    sb.append("      xmlns:ib=\"http://ULIS.immobilier.sopragroup.com/faces/html\"\n");
    sb.append("      xmlns:ip=\"http://personalization.immobilier.sopragroup.com\"\n");
    sb.append("      xmlns:ifn=\"http://immobilier.sopragroup.com/faces/functions\">\n");
    sb.append("<!-- \n");
    sb.append(" *\n");
    sb.append(" * La recherche.\n");
    sb.append(" *\n");
    sb.append(" * Nom du fichier : $HeadURL: http://svn.lyon.fr.sopra/svnulis/svn/ulis_test/repository/Refonte/ULISNG/ $\n");
    sb.append(" * Version        : $Revision: 1.0 $\n");
    sb.append(" *\n");
    sb.append(" * Auteur         : SOPRA Group - GTH\n");
    sb.append(" * Auteur         : SOPRA Group - $Author: GTH $\n");
    sb.append(" * Modifié le     : $Date: 2011-06-06 15:52:35 +0200 (lun, 06 jun 2011) $\n");
    sb.append(" *\n");
    sb.append(" * @since Kernel 6.0\n");
    sb.append(" *\n");
    sb.append(" -->\n");
    sb.append("<ih:CLASS_VERSION value=\"$Revision: 1.0 $\" />\n");


    return sb.toString();
  }
  
  private static String declareEnteteForPanier(){
    StringBuilder sb = new StringBuilder();

    sb.append("<ih:body  buttonBar=\"shoppingCart\">\n");
    sb.append("\n");
    sb.append("  <ih:globalErrors/>\n");
    return sb.toString();
  }
  
  private static String declareEnteteForRecherche(){
    StringBuilder sb = new StringBuilder();
    sb.append("<ih:body buttonBar=\"search\">\n");
    sb.append("<ui:define name=\"title\">#{pathPersonalization.definition.LIB}</ui:define>\n");
    sb.append("  <ih:globalErrors/>\n");
    return sb.toString();
  }
  
  private static String declareEnteteForGestion(){
    StringBuilder sb = new StringBuilder();
    sb.append("<ih:body buttonBar=\"true\" toolBar=\"business\">\n");
    sb.append("\n");
    sb.append("<ui:define name=\"buttonBar\">\n");
    sb.append("  <ih:grid styleClass=\"buttonBar GRID\" ignoreEmptyCell=\"false\">\n");
    sb.append("    <ih:row>\n");
    sb.append("      <ih:cell/>        \n");
    sb.append("      <ih:cell addStyleClass=\"last\">\n");
    sb.append("       <ih:continue/>\n");
    sb.append("      </ih:cell>\n");
    sb.append("      <ih:cell>\n");
    sb.append("        <ih:abort defaultButton=\"false\" />\n");
    sb.append("      </ih:cell>\n");
    sb.append("    </ih:row>\n");
    sb.append("  </ih:grid>\n");
    sb.append("</ui:define>\n");
    sb.append("\n");
    sb.append("<ih:screenActionLinks>\n");
    sb.append("  <ui:include src=\"/WF/fgEtapeActionLinks.xhtml\">\n");
    sb.append("    <ui:param name=\"WFEtapeCourante\" value=\"#{mereEntity.occurrence.etape}\"/>\n");
    sb.append("  </ui:include>\n");
    sb.append("  <ih:actionCommandLink key=\""+packageName+"."+name+".liensVoulezVous.lblSuppression\" \n");
    sb.append("                        action=\"#{screenController.supprimer}\" \n");
    sb.append("                        immediate=\"true\"/>\n");

    sb.append("</ih:screenActionLinks> \n");
    sb.append("\n");
    sb.append("<ui:include src=\"/WF/fgEtapeWorkflow.xhtml\">\n");
    sb.append("  <ui:param name=\"etapeEntityData\" value=\"#{mereEntity.occurrence.etape}\" />\n");
    sb.append("</ui:include>\n");
    sb.append("\n");
    sb.append("<ih:globalErrors />\n");

    return sb.toString();
  }
  
  private static String declarePiedPage(){
    StringBuilder sb = new StringBuilder();
    sb.append("</ih:body>\n");
    sb.append("</html>\n");
    //fin de la page xhtml
    return sb.toString();
  }
  
  private static String declareListeResultatForPanier(List<Proxy> listeProxy, boolean pourPanier, boolean pourSynthese){
    StringBuilder sb = new StringBuilder();
    sb.append("<ih:multiBlock id=\"listeResultats\" entityData=\"#{listeResultats}\" key=\""+packageName+"."+name+suffixe+".titBlocListe\" rendered=\"#{model.showSearchResults}\">\n");
    sb.append("\n");
    sb.append("<ih:dataTable id=\"tableListeResultats\" value=\"#{listeResultats.active}\" var=\"currentResultat\">\n");
    if ( pourPanier){
      sb.append("  <ih:column>\n");
      sb.append("   <f:facet name=\"header\">\n");
      sb.append("     <ih:insertAll />\n");
      sb.append("  </f:facet>\n");
      sb.append("  <ih:insertItem />\n");
      sb.append("</ih:column>\n");
    }
    sb.append(" \n");
    for (Proxy donnee : listeProxy) {
      sb.append("<ih:column");
      if (donnee.isTriable()){
        sb.append(" sort=\""+donnee.getNom()+"\"");
      } 
      sb.append(">\n");
      
      //declaration du cham
  
      if (donnee.getType().contains("Boolean")){
        sb.append("  <ih:selectBooleanCheckbox");
      } 
      else  if (donnee.getType().contains("BigDecimal")){
        sb.append("  <ih:output");
         sb.append("Decimal");
      }else if (donnee.getType().contains("String")){
        sb.append("  <ih:output");
        sb.append("Text");
      }else{
        sb.append("  <ih:output");
        sb.append(donnee.getType());
      }
      if (!donnee.isLien()){  sb.append(" value=\"#{currentResultat."+donnee.getNom()+"}\"");      }
      
      sb.append(" key=\""+packageName+"."+name+suffixe+".liste.lbl"+donnee.getNom()+"\" "); 
      
      if (donnee.isLien()){sb.append(" size=\"1\" rendered=\"false\" ");}
      
      sb.append("/> \n");

      if (donnee.isLien()){
        sb.append("  <ih:commandLink id=\""+donnee.getNom()+"\"\n");
        sb.append("                  action=\"invoke:DESTINATION\"\n");
        sb.append("                  value=\"#{currentResultat."+donnee.getNom()+"}\">\n");
        sb.append("    <if:parameter name=\"PARAM_NAME1\" value=\"#{currentResultat.VALEUR}\" />\n");
        sb.append("    <if:parameter name=\"PARAM_NAME2\" value=\"XX\" />\n");
        sb.append("  </ih:commandLink>\n");
      }
      //fin du champ
      sb.append("</ih:column>\n");
      sb.append("\n");
      //fin de la colonne
    }
    
    //fin du bloc de resultat
    sb.append("</ih:dataTable>\n");
    sb.append("</ih:multiBlock>\n");
    
    
    return sb.toString();
  }
  
  private static String declareListeResultatForRecherche(){
    StringBuilder sb = new StringBuilder();
    sb.append("<ip:dynaList />\n");
    sb.append("\n");
    sb.append("<ih:verticalSpacer rendered=\"#{model.showSearchResults}\"/>\n");
    return sb.toString();
  }
  
  private static String declareListePanier(List<Proxy> listeProxy){
    StringBuilder sb = new StringBuilder();
    sb.append("<ih:verticalSpacer/>\n");
    sb.append("\n");
    sb.append("<ih:shoppingCartBlock id=\"shoppingCartReservation\" key=\""+packageName+"."+name+suffixe+".titBlocPanier\" entityData=\"#{shoppingCart}\" defaultStatus=\"complete\" >\n");
    sb.append("\n");
    sb.append(" <ih:blockHeader>\n");
    sb.append("  <ui:remove>TODO On n'arrive pas utiliser EL, donc on utilise un string pour l'instant</ui:remove>\n");
    sb.append("  <ih:blockStatus changeStatusActionListener=\"shoppingCart.entity.controller.changeStatusMode\" />\n");
    sb.append("</ih:blockHeader>\n");
    sb.append("\n");
    sb.append("<h:panelGroup rendered=\"#{shoppingCart.count > 0}\">\n");
    sb.append("  <ih:dataTable value=\"#{shoppingCart.active}\" var=\"selected\">\n");
    
    
    for (Proxy donnee : listeProxy) {
      sb.append("<ih:column");
      if (donnee.isTriable()){
        sb.append(" sort=\""+donnee.getNom()+"\"");
      } 
      sb.append(">\n");
      
      //declaration du champ
      sb.append("  <ih:output");
      if (donnee.getType().contains("BigDecimal")){
        sb.append("Decimal");
      }else if (donnee.getType().contains("String")){
        sb.append("Text");
      }else{
        sb.append(donnee.getType());
      }
      if (!donnee.isLien()){  sb.append(" value=\"#{currentResultat."+donnee.getNom()+"}\"");      }
      
      sb.append(" key=\""+packageName+"."+name+suffixe+".liste.lbl"+donnee.getNom()+"\" "); 
      
      if (donnee.isLien()){sb.append(" size=\"1\" rendered=\"false\" ");}
      
      sb.append("/> ");

      if (donnee.isLien()){
        sb.append("  <ih:commandLink id=\""+donnee.getNom()+"\"\n");
        sb.append("                  action=\"invoke:DESTINATION\"\n");
        sb.append("                  value=\"#{currentResultat."+donnee.getNom()+"}\">\n");
        sb.append("    <if:parameter name=\"PARAM_NAME1\" value=\"#{currentResultat.VALEUR}\" />\n");
        sb.append("    <if:parameter name=\"PARAM_NAME2\" value=\"XX\" />\n");
        sb.append("  </ih:commandLink>\n");
      }
      //fin du champ
      sb.append("</ih:column>\n");
      sb.append("\n");
      //fin de la colonne
    }
    //ajout de la colonne de suppression
    sb.append("  <ih:column specialColumn=\"true\">\n");
    sb.append("      <f:facet name=\"header\">\n");
    sb.append("        <ih:removeAll />\n");
    sb.append("      </f:facet>\n");
    sb.append("      <ih:removeItem occurrence=\"#{demande}\"/>\n");
    sb.append("  </ih:column>\n");
    sb.append("  </ih:dataTable>\n");
    sb.append("</h:panelGroup>\n");
    sb.append("\n");
    sb.append("</ih:shoppingCartBlock>\n");
    return sb.toString();
  }
  
  private static String declareBloc(List<Criteria> listeCriteria){
    StringBuilder sb = new StringBuilder();
    if (listeBloc.isEmpty()){
      //ajout une bloc null
      listeBloc.add(null);
      
      sb.append("<ih:verticalSpacer/>\n");
      sb.append("\n");
      sb.append("<ih:criteriaBlock logicalName=\"\" key=\""+packageName+"."+name+suffixe+".titBlocCriteres\" entityData=\"#{rechCriteria}\" entityVar=\"criteria\">\n");
      sb.append("  <ih:grid>\n");
      sb.append("  \n");
      
      sb.append(declareCriteria(listeCriteria, null));
      sb.append("     \n");
      sb.append("    </ih:grid>\n");
      sb.append("  </ih:criteriaBlock>\n");
    }else{
    //génaration des bloc
      for (TypeBloc bloc : listeBloc) { 
        sb.append("<ih:verticalSpacer/>\n");
        sb.append("\n");
        sb.append("<ih:criteriaBlock logicalName=\""+bloc.getLogicalName()+"\" key=\""+packageName+"."+name+suffixe+".titBloc"+bloc.getCode()+"\" entityData=\"#{rechCriteria}\" entityVar=\"criteria\" ");
        if (bloc.isReduitParDefaut()){
          sb.append("defaultStatus=\"reduced\"");
        }
        
        sb.append(">\n");
        sb.append("  <ih:grid>\n");
        sb.append("  \n");
        
        sb.append(declareCriteria(listeCriteria, bloc));
        sb.append("     \n");
        sb.append("    </ih:grid>\n");
        sb.append("  </ih:criteriaBlock>\n");
        indexBloc++;
      }
      
    }
    
    indexBloc=0;
    return sb.toString();
  }
  
  private static String declareBlocForGestion(List<Criteria> listeCriteria){
    StringBuilder sb = new StringBuilder();
    if (listeBloc.isEmpty()){
      //ajout une bloc null
      listeBloc.add(null);
      
      sb.append("<ih:verticalSpacer/>\n");
      sb.append("\n");
      sb.append("<ih:singleBlock logicalName=\"\" key=\""+packageName+"."+name+suffixe+".titBlocCriteres\" entityData=\"#{rechCriteria}\" entityVar=\"criteria\">\n");
      sb.append("  <ih:grid>\n");
      sb.append("  \n");
      
      sb.append(declareCriteria(listeCriteria, null));
      sb.append("     \n");
      sb.append("    </ih:grid>\n");
      sb.append("  </ih:singleBlock>\n");
    }else{
    //génaration des bloc
      for (TypeBloc bloc : listeBloc) { 
        sb.append("<ih:verticalSpacer/>\n");
        sb.append("\n");
        sb.append("<ih:singleBlock logicalName=\""+bloc.getLogicalName()+"\" key=\""+packageName+"."+name+suffixe+".titBloc"+bloc.getCode()+"\" entityData=\"#{mereEntity}\" entityVar=\"criteria\" ");
        if (bloc.isReduitParDefaut()){
          sb.append("defaultStatus=\"reduced\"");
        }
        
        sb.append(">\n");
        sb.append("  <ih:grid>\n");
        sb.append("  \n");
        
        sb.append(declareCriteria(listeCriteria, bloc));
        sb.append("     \n");
        sb.append("    </ih:grid>\n");
        sb.append("  </ih:singleBlock>\n");
        indexBloc++;
      }
      
    }
    
    indexBloc=0;
    return sb.toString();
  }
  
  private static String declareCriteria(List<Criteria> listeCriteria,TypeBloc bloc){
    StringBuilder sb = new StringBuilder();
    float indexUneLigneSurDeux=0;
    int index=0;
    boolean ligneOuverte=false;
    boolean ligneFerme=false;
    boolean logicalOuvert=false;
    String propertyCourante="";
    for (Criteria critere : listeCriteria) {
      if ((critere.getBloc()==null && indexBloc==0)  ||(critere.getBloc()!=null && critere.getBloc().equals(bloc)) ){//ajout du criteria sans bloc dans le premier bloc de la liste
        
        if (critere.getProperty()!=null && !"".equals(critere.getProperty()) && propertyCourante.equals("")){
          propertyCourante=critere.getProperty();
          //ouvrir le logicalField
          sb.append("    <ih:row>\n");
          sb.append("      <ih:cell>\n");
          sb.append("<ih:logicalField key=\""+packageName+"."+name+".criteria.lbl"+critere.getNom()+"\"\n");
          sb.append("  property=\""+propertyCourante+"\">\n");
          sb.append("<ih:grid>\n");
          logicalOuvert=true;
        }
        if ((critere.getProperty()==null || !critere.getProperty().equals(propertyCourante) ) && logicalOuvert){
          propertyCourante="";
          //fermer le logicalField
          sb.append("</ih:grid>\n");
          sb.append("</ih:logicalField>\n");
          sb.append("    /<ih:row>\n");
          sb.append("    /<ih:cell>\n");
          logicalOuvert=false;
        }
        
        
        if (indexUneLigneSurDeux==2){indexUneLigneSurDeux=0;}//remise a zero du compteur
        
        //Creation d'une ligne
        if (new Float(indexUneLigneSurDeux/2).equals(new Float(0)) ){
          ligneOuverte=true;
          ligneFerme=false;
          sb.append("    <ih:row>\n");
        }
        
        //Debut Champ
        /*if (!"".equals(propertyCourante)){
          //on ouvre une ligne a chaque fois 
          sb.append("    <ih:row>\n");
        }*/
        sb.append("      <ih:cell>\n");
      
      
      if (critere.getListeDeroulante()!=null){
        //traitement d'une liste
        
        sb.append("       <ih:selectOneMenu property=\""+critere.getNom()+"\" key=\""+packageName+"."+name+suffixe+".criteria.lbl"+critere.getNom()+"\" value=\"#{criteria."+critere.getNom()+"}\" ");
        if (critere.getListeDeroulante().isEmptyChoice()){
          sb.append("        includeEmptyChoice=\"true\" emptyChoiceKey=\""+packageName+".lblValeurVideListe\"");
        }
        sb.append("        >\n");
        if (critere.getListeDeroulante().getType()!=null && !"MAN".equalsIgnoreCase(critere.getListeDeroulante().getType())){
          sb.append("                           <f:selectItems value=\"#{criteria.dependencies.list"+critere.getNom()+"}\" var=\"item"+critere.getNom()+"\"\n");
          if (critere.getListeDeroulante().getTypeAffichage().equalsIgnoreCase("COD_LIB") 
            || critere.getListeDeroulante().getTypeAffichage().equalsIgnoreCase("LIB")
            || critere.getListeDeroulante().getTypeAffichage().equalsIgnoreCase("LIC")){
            sb.append("                           itemValue=\"#{item"+critere.getNom()+".COD}\"\n");
            sb.append("                           itemLabel=\"#{item"+critere.getNom()+".LABEL}\"\n");  
          }else if (critere.getListeDeroulante().getTypeAffichage().equalsIgnoreCase("COD")) {
            sb.append("                           itemValue=\"#{item"+critere.getNom()+".COD}\"\n");
            sb.append("                           itemLabel=\"#{item"+critere.getNom()+".COD}\"\n");  
          }
          sb.append("                           itemDisabled=\"#{(item"+critere.getNom()+".COD != criteria."+critere.getNom()+") and item"+critere.getNom()+".PER}\"/>\n");
          sb.append("                       </ih:selectOneMenu>\n");
        }else{
          for(Map.Entry<String, String> e:critere.getListeDeroulante().getListeOption().entrySet()){
            sb.append("                           <f:selectItem itemValue=\""+e.getKey()+"\" itemLabel=\"#{i18n['"+packageName+"."+name+suffixe+".criteria.opt"+critere.getNom()+"_"+e.getKey()+"']}\" />\n");          
          }
          sb.append("                       </ih:selectOneMenu>\n");
        }
      }else if(critere.getPanier()!=null){
        //traitement d'un panier
        sb.append("<ih:shoppingCart property=\"panier"+critere.getNom()+"\" key=\""+packageName+"."+name+suffixe+".criteria.lbl"+critere.getNom()+"\" value=\"#{criteria."+critere.getNom()+"}\" />\n");
      }else if(critere.getComposantMetier()!=null){
        //traitement d'un composant metier
        if (critere.getComposantMetier().getType().contains("Ref") || critere.getComposantMetier().getType().contains("observation")){
          if (critere.getComposantMetier().getType().contains("observation")){
            sb.append("<ih:input"+critere.getComposantMetier().getType()+" property=\""+critere.getNom()+"\" key=\""+packageName+"."+name+suffixe+".criteria.lbl"+critere.getNom()+"\" value=\"#{criteria."+critere.getComposantMetier().getType().toUpperCase()+"}\" />\n");
          }else{
            sb.append("<ib:input"+critere.getComposantMetier().getType()+" property=\""+critere.getNom()+"\" key=\""+packageName+"."+name+suffixe+".criteria.lbl"+critere.getNom()+"\" value=\"#{criteria."+critere.getComposantMetier().getType().toUpperCase()+"}\" />\n");
          }
        }else{
        //@TODO si plusieurs PAESI dans la meme page utiliser l attribut nomCriteriaName
        sb.append("<ib:input"+critere.getComposantMetier().getType().toUpperCase()+"Criterion property=\""+critere.getNom()+"\" key=\""+packageName+"."+name+suffixe+".criteria.lbl"+critere.getNom()+"\" value=\"#{criteria."+critere.getComposantMetier().getType().toUpperCase()+"}\" />\n");
        }
      }else if(critere.getFastInput()!=null){
        ////traitement d'un fastInput
        sb.append("<ih:logicalField property=\""+critere.getNom()+"\" key=\""+packageName+"."+name+suffixe+".criteria.lbl"+critere.getNom()+"\">\n");
        sb.append("<ih:fastInputDefine property=\""+critere.getNom()+"_FastInput\" >  \n");
        
        sb.append(debutChamp(critere));
        
        sb.append(" property=\""+critere.getNom()+"_INNER\" value=\"#{criteria."+critere.getNom()+"}\" />\n");
        sb.append("<ih:fastInputLink/>\n");
        sb.append("</ih:fastInputDefine>\n");
        sb.append("</ih:logicalField>\n");
        
      }else{
        sb.append(debutChamp(critere));
        sb.append(" property=\""+critere.getNom()+"\" key=\""+packageName+"."+name+suffixe+".criteria.lbl"+critere.getNom()+"\"\n"); 
        sb.append("                   value=\"#{criteria."+critere.getNom()+"}\" />\n");
      }
      

      
      
      //Fin Champ
      sb.append("     </ih:cell>\n");
      //Fermeture de la ligne
      if ((!new Float(indexUneLigneSurDeux/2).equals(new Float(0)) || listeCriteria.size()==(index+1))&& "".equals(propertyCourante)){
        sb.append("   </ih:row>\n");
        ligneOuverte=false;
        ligneFerme=true;
      }
      if (!"".equals(propertyCourante)){
        //on ferme une ligne a chaque fois 
        sb.append("    </ih:row>\n");
      }
      
      if ("".equals(propertyCourante)){
        indexUneLigneSurDeux++;
      }
      index++;
    }
    }
    if (ligneOuverte && !ligneFerme){
      sb.append("   </ih:row>\n");
      ligneOuverte=false;
      ligneFerme=true;
    }
    
    return sb.toString();
  }
  
  private static String debutChamp(Criteria critere){
    StringBuilder sb = new StringBuilder();
    if (critere.getType().contains("Boolean")){
      sb.append("  <ih:selectBooleanCheckbox");
    } else if (critere.getType().contains("BigDecimal")){
      sb.append("        <ih:input");
      sb.append("Decimal");
    }else if (critere.getType().contains("String")){
      sb.append("        <ih:input");
      sb.append("Text");
    }else if (critere.getType().contains("Integer")){
      sb.append("        <ih:input");
      sb.append("Integer useGrouping=\"false\" ");
      
    }else{
      sb.append("        <ih:input");
      sb.append(critere.getType());
    }
    return sb.toString();
  }
  
  private static String generatePropertie(String pathOutput,List<Criteria> listeCriteria,List<Proxy> listeProxy){
    StringBuilder sb = new StringBuilder();
    try {
      File file = new File(pathOutput+name+suffixe+".propertie");
      BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
      //generation titreBlocList
      out.append(packageName+"."+name+suffixe+".titPage=\n");
      out.append(packageName+"."+name+suffixe+".titBlocListe=\n");
      out.append(packageName+"."+name+suffixe+".titBlocPanier=\n");
      out.append(packageName+"."+name+suffixe+".lblValeurVideListe=-\n");
      if (!listeBloc.isEmpty()){
        for (TypeBloc bloc : listeBloc) {
          if (bloc!=null){
          out.append(packageName+"."+name+suffixe+".titBloc"+bloc.getCode()+"="+bloc.getLibelle()+"\n");
          }
        }
      }else {
        out.append(packageName+"."+name+suffixe+".titBlocCriteres=\n");
      }
      if (listeProxy!=null){
      for (Proxy donnee : listeProxy) {
        out.append(packageName+"."+name+suffixe+".liste.lbl"+donnee.getNom().trim()+"="+donnee.getDescription().trim()+"\n");
      }
      }
      for (Criteria donnee : listeCriteria) {
        out.append(packageName+"."+name+suffixe+".criteria.lbl"+donnee.getNom().trim()+"="+donnee.getDescription().trim()+"\n");
        if (donnee.getListeDeroulante()!=null && donnee.getListeDeroulante().getType()!=null && "MAN".equalsIgnoreCase(donnee.getListeDeroulante().getType())){
          for(Map.Entry<String, String> e:donnee.getListeDeroulante().getListeOption().entrySet()){
            out.append(packageName+"."+name+suffixe+".criteria.opt"+donnee.getNom().trim()+"_"+e.getKey()+"="+e.getValue().trim()+"\n");
          }
        }
        if (donnee.getControle()!=null && donnee.getControle().getCodeErreur()!=null && donnee.getControle().getLibelleErreur()!=null){
          out.append(packageName+"."+name+suffixe+".err"+donnee.getControle().getCodeErreur().trim()+"="+donnee.getControle().getLibelleErreur().trim()+"\n");
        }
      }
      
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return sb.toString();
  }
  
  private static List<TypeBloc> extractionBloc(List<Criteria> listeCriteria){
    listeBloc = new ArrayList();
    for (Criteria donnee : listeCriteria) {
      if (donnee.getBloc()!=null){
        TypeBloc bloc=donnee.getBloc();
        TypeBloc blocPresent=containsBloc(bloc.getCode());
        if (blocPresent!=null){
          String libellePresent=blocPresent.getLibelle();
          if (libellePresent.isEmpty() && !bloc.getLibelle().isEmpty() ){
            //ajout du libellé au bloc déjá present dans la liset 
            blocPresent.setLibelle(bloc.getLibelle());
          }
          if (!blocPresent.isReduitParDefaut() && bloc.isReduitParDefaut() ){
            //ajout du libellé au bloc déjá present dans la liset 
            blocPresent.setReduitParDefaut(bloc.isReduitParDefaut());
          }
        }else{
          TypeBloc blocNouveau=new TypeBloc();
          blocNouveau.setCode(bloc.getCode().trim());
          blocNouveau.setLibelle(bloc.getLibelle().trim());
          blocNouveau.setReduitParDefaut(bloc.isReduitParDefaut());
          listeBloc.add(blocNouveau);  
        }
      }
    }
    return listeBloc; 
  }
  
  private static TypeBloc containsBloc(String code){
    for (TypeBloc bloc : listeBloc) {
      if (bloc.getCode().equalsIgnoreCase(code)){
        return bloc;
      }
    }
    return null;
  }
}


