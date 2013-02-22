
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
public class TypeComposantMetier {
  
  /** Version extraite du gestionnaire de configuration */
  public static final String CLASS_VERSION = "$Revision:   1.0  $";
  
  private String type;//paesi/toeso...
  private String nomCriterionName;//Falcultatif, s'il y a plusieur paesi dans la meme page il faut remplir ce champ
  
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getNomCriterionName() {
    return nomCriterionName;
  }

  public void setNomCriterionName(String nomCriterionName) {
    this.nomCriterionName = nomCriterionName;
  }
  
  
  
}
