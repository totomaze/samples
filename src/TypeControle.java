

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
public class TypeControle {
  
  /** Version extraite du gestionnaire de configuration */
  public static final String CLASS_VERSION = "$Revision:   1.0  $";
  
  private String codeErreur;//code du bloc
  private String libelleErreur;//libelle facultatif
  
  /**
   * @return la valeur positionnée au sein de la propriété
   */
  public String getCodeErreur() {
    return codeErreur;
  }
  
  /**
   * @param codeErreur valeur à positionner au sein de la propriété
   */
  public void setCodeErreur(String codeErreur) {
    this.codeErreur = codeErreur;
  }
  
  /**
   * @return la valeur positionnée au sein de la propriété
   */
  public String getLibelleErreur() {
    return libelleErreur;
  }
  
  /**
   * @param libelleErreur valeur à positionner au sein de la propriété
   */
  public void setLibelleErreur(String libelleErreur) {
    this.libelleErreur = libelleErreur;
  }

}
