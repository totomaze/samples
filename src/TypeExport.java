import java.util.List;


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
public class TypeExport {
  
  /** Version extraite du gestionnaire de configuration */
  public static final String CLASS_VERSION = "$Revision:   1.0  $";
  
  private String codeTable;//code du bloc
  private List<String> listeChampExport;//liste des champ présent dans l'export

  public String getCodeTable() {
    return codeTable;
  }
  public void setCodeTable(String codeTable) {
    this.codeTable = codeTable;
  }
  public List<String> getListeChampExport() {
    return listeChampExport;
  }
  public void setListeChampExport(List<String> listeChampExport) {
    this.listeChampExport = listeChampExport;
  }
    
  }

