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
public class TypeListeDeroulante {
  
  /** Version extraite du gestionnaire de configuration */
  public static final String CLASS_VERSION = "$Revision:   1.0  $";
  
  private String type;//SPE/COD/TAB/MAN
  private String nomRegle;//pour liste de type SPE
  private String typeAffichage;//COD_LIB / COD
  private String nomCodif;//pour liste de type COD
  private String nomTable;//pour liste de type TAB
  private String nomChampCod;//pour liste de type TAB
  private String nomChampLib;//pour liste de type TAB
  private String nomChampEtat;//pour liste de type TAB
  private Map<String, String> listeOption;//pour liste de type MAN (liste(COD_LIB,MAN,{A:Aucun,T:libellé Tiers}[,empty]))  []optionnel les {} sont obligatoires
  //extra Type (empty)
  private boolean emptyChoice=false;// mis à true si l'extratype contient le mot empty
  
  
  
  /**
   * @return la valeur positionnée au sein de la propriété
   */
  public String getType() {
    return type;
  }
  
  /**
   * @param type valeur à positionner au sein de la propriété
   */
  public void setType(String type) {
    this.type = type;
  }
  
  /**
   * @return la valeur positionnée au sein de la propriété
   */
  public String getNomRegle() {
    return nomRegle;
  }
  
  /**
   * @param nomRegle valeur à positionner au sein de la propriété
   */
  public void setNomRegle(String nomRegle) {
    this.nomRegle = nomRegle;
  }
  
  /**
   * @return la valeur positionnée au sein de la propriété
   */
  public String getTypeAffichage() {
    return typeAffichage;
  }
  
  /**
   * @param typeAffichage valeur à positionner au sein de la propriété
   */
  public void setTypeAffichage(String typeAffichage) {
    this.typeAffichage = typeAffichage;
  }
  
  /**
   * @return la valeur positionnée au sein de la propriété
   */
  public String getNomCodif() {
    return nomCodif;
  }
  
  /**
   * @param nomCodif valeur à positionner au sein de la propriété
   */
  public void setNomCodif(String nomCodif) {
    this.nomCodif = nomCodif;
  }
  
  /**
   * @return la valeur positionnée au sein de la propriété
   */
  public String getNomTable() {
    return nomTable;
  }
  public void setNomTable(String nomTable) {
    this.nomTable = nomTable;
  }
  public String getNomChampCod() {
    return nomChampCod;
  }
  public void setNomChampCod(String nomChampCod) {
    this.nomChampCod = nomChampCod;
  }

  /**
   * @return la valeur positionnée au sein de la propriété
   */
  public String getNomChampEtat() {
    return nomChampEtat;
  }

  /**
   * @param nomChampEtat valeur à positionner au sein de la propriété
   */
  public void setNomChampEtat(String nomChampEtat) {
    this.nomChampEtat = nomChampEtat;
  }

  
  /**
   * @return la valeur positionnée au sein de la propriété
   */
  public Map<String, String> getListeOption() {
    return listeOption;
  }

  public void setListeOption(Map<String, String> listeOption) {
    this.listeOption = listeOption;
  }

  public boolean isEmptyChoice() {
    return emptyChoice;
  }

  public void setEmptyChoice(boolean emptyChoice) {
    this.emptyChoice = emptyChoice;
  }

  public String getNomChampLib() {
    return nomChampLib;
  }

  public void setNomChampLib(String nomChampLib) {
    this.nomChampLib = nomChampLib;
  }
  
  
}
