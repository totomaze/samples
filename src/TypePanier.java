
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
public class TypePanier {
  
  /** Version extraite du gestionnaire de configuration */
  public static final String CLASS_VERSION = "$Revision:   1.0  $";
  
  private String type;//generique/fonction
  private String nomFonction;//pour liste de type fonction
  private String nomChampCod;//ex:TOESO_COD pour liste de type generique
  private String nomChampLib;//ex:TOESO_LIB pour liste de type generique
  private String nomTable;//TOESO pour liste de type generique
  private String nomChampEtat;//TOESO_ETA pour liste de type generique FACULTATIF
  private String ETAT;//A pour liste de type generique  FACULTATIF
  private String clause;//clause (TONEO_COD = 25 ) pour liste de type generique FACULTATIF
  
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getNomFonction() {
    return nomFonction;
  }
  public void setNomFonction(String nomFonction) {
    this.nomFonction = nomFonction;
  }
  public String getNomChampCod() {
    return nomChampCod;
  }
  public void setNomChampCod(String nomChampCod) {
    this.nomChampCod = nomChampCod;
  }
  public String getNomChampLib() {
    return nomChampLib;
  }
  public void setNomChampLib(String nomChampLib) {
    this.nomChampLib = nomChampLib;
  }
  public String getNomTable() {
    return nomTable;
  }
  public void setNomTable(String nomTable) {
    this.nomTable = nomTable;
  }
  public String getNomChampEtat() {
    return nomChampEtat;
  }
  public void setNomChampEtat(String nomChampEtat) {
    this.nomChampEtat = nomChampEtat;
  }
  public String getETAT() {
    return ETAT;
  }
  public void setETAT(String eTAT) {
    ETAT = eTAT;
  }
  public String getClause() {
    return clause;
  }
  public void setClause(String clause) {
    this.clause = clause;
  }
  
  
}
