import java.util.StringTokenizer;


/**
 * Cette classe encapsule le nom, le type de chaque colonne
 * de la table ainsi que la longueur du champ et des d�cimaux s'il en a
 * cette classe contient l�information d�un colonne. 
 * Pour chaque colonne de la table en base, un objet de ce type doit �tre cr��
 * Chaque ligne du fichier <nom_table>.table doit contenir les 6 champs suivants obligatoirement dans cette ordre et s�par�s par des espaces blancs
 * Nom de la colonne
 * Type java correspondant : Integer, String, Date(en minuscule), BigDecimal sont lus pour l�instant. Il faut compl�ter le parser pour d�autre type
 * Taille du champ
 * FractionDigit : si ce n�est pas un decimal, un 0 doit �tre inclus
 * isNullable : ce champ doit contenir Yes ou No si la colonne peut contenir un null
 * isKey : ce champ doit contenir Yes ou No si la colonne contient un membre de la cl� primaire
 * @author dalonso
 *
 */

public class DataTypeMandatory {
	private String tableName;
	private String name;
	private String type;
	private boolean nullable;
	private boolean key;
	private int length;
	private int fractionDigits;
	
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public boolean isKey() {
		return key;
	}
	public void setKey(boolean key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isNullable() {
		return nullable;
	}
	public void setNullable(boolean mnullable) {
		this.nullable = mnullable;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getFractionDigits() {
		return fractionDigits;
	}
	public void setFractionDigits(int fractionDigits) {
		this.fractionDigits = fractionDigits;
	}
	public DataTypeMandatory(String tableName,String ligne) {
		setTableName(tableName);
		StringTokenizer st = new StringTokenizer(ligne);
		
		if(st.countTokens()!=6){
			System.out.println("Ligne: -" +ligne+"- mal format�.");
			System.out.println("6 champs s�par�s par des espaces blancs. Nom Type longueur decimaux isMandatory isKey");
		}
		setName(st.nextToken());
		setType(st.nextToken());
		setLength(Integer.parseInt(st.nextToken()));
		setFractionDigits(Integer.parseInt(st.nextToken()));
		if(st.nextToken().equalsIgnoreCase("Yes")){
			setNullable(true);
		}else{
			setNullable(false);
		}
		if(st.nextToken().equalsIgnoreCase("Yes")){
			setKey(true);
		}else{
			setKey(false);
		}
	}
	
	
	@Override
	public String toString() {
		return "DataTypeMandatory [name=" + name + ", type=" + type
				+ ", nullable=" + nullable + ", key=" + key + ", length="
				+ length + ", fractionDigits=" + fractionDigits + "]";
	}
	
	public String declareConstante(){
		StringBuilder sb = new StringBuilder("/** ");
		sb.append(name).append(" */\n");
		sb.append("private ").append(type).append(" ").append("m").append(name).append(" = null;");
		return sb.toString();
	}
	
	public String declareGetterForBaseProxy(){		
		StringBuilder sb = new StringBuilder(" \n /**\n   * @return la valeur positionn�e au sein de la propri�t�\n   */\n");
		sb.append("public ").append(type).append(" get").append(name).append("(){\n");
		sb.append("\t return m").append(name).append(";\n");
		sb.append("}\n");
		return sb.toString();
	}
	
	public String declareSetterForBaseProxy(){		
		StringBuilder sb = new StringBuilder("  /**\n   * @param ").append(name).append(" valeur � positionner au sein de la propri�t�\n   */\n");
		sb.append("public void set").append(name).append("(").append(type).append(" ").append(name).append("){\n");
		sb.append("if (isLoggingDebug())\n");
		sb.append("\t logDebug(\" set").append(name).append("() <-- \" + ").append(name).append(");\n");
		sb.append("m").append(name).append(" = ").append(name).append(";\n");
		sb.append("}");
		return sb.toString();
	}
	
	public String declareParameters(){
		StringBuilder sb = new StringBuilder("/** Nom du param�tre donnant acc�s au ").append(name).append(" */\n");
		sb.append("public static final String ").append(name).append("_PARAMETER_NAME = \"").append(name).append("\";\n");
		return sb.toString();
	}
	
	public String declareGetterParametersForService(){		
		Integer max = new Integer(10^length);
		max = max-1;
		StringBuilder sb = new StringBuilder("");
		/*if(!nullable){
			sb.append("@RequiredConstraint \n");
		}
		if(type.equalsIgnoreCase("Integer")){
			sb.append("@ForAnInteger \n");
			sb.append("@Max(").append(max).append(")\n");
		}
		if(type.equalsIgnoreCase("Date")){
			sb.append("@ForADate \n");
		}
		if(type.equalsIgnoreCase("String")){
			sb.append("@MaxLengthConstraint(").append(length).append(")\n");
		}
		if(type.equalsIgnoreCase("BigDecimal")){
			sb.append("@ForADecimal(digits=").append(length).append(",fractionDigits=").append(fractionDigits).append(") \n");
		}*/
		sb.append("public ").append(type).append(" get").append(name).append("(){\n");
		sb.append("\t return getParameterValueAs").append(type).append("(").append(name).append("_PARAMETER_NAME);\n");
		sb.append("}\n");
		return sb.toString();
	}
	
	
	public String declareCriteria(){
		StringBuilder sb = new StringBuilder("/** Nom du crit�re donnant acc�s au ").append(name).append(" */\n");
		sb.append("public static final String ").append(name).append("_CRITERION_NAME = \"").append(name).append("\";\n");
		return sb.toString();
	}
	
	public String declareGetterCriteriaForService(){
		int j=1;
		for (int i = 0; i < length; i++) {
			j=j*10;
		}
		Integer max = new Integer(j);
		max = max-1;
		StringBuilder sb = new StringBuilder(" \n /**\n   * @return la valeur du crit�re \n   */\n");
		if(!nullable){
			sb.append("@RequiredConstraint \n");
		}
		if(type.equalsIgnoreCase("Integer")){
			sb.append("@ForAnInteger \n");
			sb.append("@Max(").append(max).append(")\n");
		}
		if(type.equalsIgnoreCase("Date")){
			sb.append("@ForADate \n");
		}
		if(type.equalsIgnoreCase("String")){
			sb.append("@MaxLengthConstraint(").append(length).append(")\n");
		}
		if(type.equalsIgnoreCase("BigDecimal")){
			sb.append("@ForADecimal(digits=").append(length).append(",fractionDigits=").append(fractionDigits).append(") \n");
		}
		sb.append("public ").append(type).append(" get").append(name).append("(){\n");
		sb.append("\t return getCriterionValueAs").append(type).append("(").append(name).append("_CRITERION_NAME);\n");
		sb.append("}\n");
		return sb.toString();
	}
	
	
	public String checkCriterionIsEmpty(){
		StringBuilder sb = new StringBuilder();
		sb.append(" if(serviceCriteria.isCriterionEmpty(").append(tableName).append("BasePrimaryKeyServiceCriteria.").append(name).append("_CRITERION_NAME)||");
		sb.append(" serviceCriteria.hasCriterionWildCard(").append(tableName).append("BasePrimaryKeyServiceCriteria.").append(name).append("_CRITERION_NAME))");
		sb.append("{\n");
		sb.append("	          addError(serviceResponse, new PropertyRequiredServiceError(").append(tableName).append("BasePrimaryKeyServiceCriteria.").append(name).append("_CRITERION_NAME));\n");
		sb.append("	        }\n");
		return sb.toString();
	}
	
	public String checkParameterIsEmpty(){
		StringBuilder sb = new StringBuilder();
		sb.append(" if(serviceParameters.isParameterEmpty(").append(tableName).append("BaseServiceParameters.").append(name).append("_PARAMETER_NAME)){\n");
		sb.append("	          addError(serviceResponse, new PropertyRequiredServiceError(\"Param�tre manquant: \",").append(tableName).append("BaseServiceParameters.").append(name).append("_PARAMETER_NAME));\n");
		sb.append("	        }\n");
		return sb.toString();
	}
	
	public String insertDataInResultSet(){
		StringBuilder sb = new StringBuilder();
		if(type.equalsIgnoreCase("Integer")){
			sb.append("if(serviceParameters.get").append(name).append("()!= null)\n");
			sb.append("rs.updateInt(\"").append(name).append("\", serviceParameters.get").append(name).append("().intValue());\n");
		}else if(type.equalsIgnoreCase("BigDecimal")){
			sb.append("if(serviceParameters.get").append(name).append("()!= null)\n");
			sb.append("rs.updateBigDecimal(\"").append(name).append("\", serviceParameters.get").append(name).append("());\n");
		}else if(type.equalsIgnoreCase("Date")){
			sb.append("if(serviceParameters.get").append(name).append("()!= null)\n");
			sb.append("rs.updateDate(\"").append(name).append("\", DateTools.getInstance().asSQLDate(serviceParameters.get").append(name).append("()));\n");
		}else if(type.equalsIgnoreCase("String")){
			sb.append("rs.updateString(\"").append(name).append("\", serviceParameters.get").append(name).append("());\n");
		}
		
		return sb.toString();
	}
	
	public String insertCriteriaStatementForSQLWHEREClause(int index){
		
		StringBuilder sb = new StringBuilder();
		if(type.equalsIgnoreCase("Integer")){
			sb.append("stmt.setInt(").append(index).append(", serviceCriteria.get").append(name).append("().intValue());\n");
		}else if(type.equalsIgnoreCase("Date")){
			sb.append("stmt.setDate(").append(index).append(", DateTools.getInstance().asSQLDate(serviceCriteria.get").append(name).append("()));\n");
		}else if(type.equalsIgnoreCase("String")){
			sb.append("stmt.setString(").append(index).append(", serviceCriteria.get").append(name).append("());\n");
		}else if(type.equalsIgnoreCase("BigDecimal")){
			sb.append("stmt.setBigDecimal(").append(index).append(", serviceCriteria.get").append(name).append("());\n");
		}
		return sb.toString();
	}

	
	public String insertCriteriaStatementForSQLWHEREClauseForUpdate(int index){
		
		StringBuilder sb = new StringBuilder();
		if(type.equalsIgnoreCase("Integer")){
			sb.append("stmt.setInt(").append(index).append(", serviceParameters.get").append(name).append("().intValue());\n");
		}else if(type.equalsIgnoreCase("Date")){
			sb.append("stmt.setDate(").append(index).append(", DateTools.getInstance().asSQLDate(serviceParameters.get").append(name).append("()));\n");
		}else if(type.equalsIgnoreCase("String")){
			sb.append("stmt.setString(").append(index).append(", serviceParameters.get").append(name).append("());\n");
		}else if(type.equalsIgnoreCase("BigDecimal")){
			sb.append("stmt.setBigDecimal(").append(index).append(", serviceParameters.get").append(name).append("());\n");
		}
		return sb.toString();
	}
}
