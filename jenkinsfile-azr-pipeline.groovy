pipeline {
	agent {
		docker {
			image 'microsoft/azure-cli'
		}
	}
    parameters {
        string(defaultValue: "", description: 'Nome Resource Group', name: 'NAME')
    }
	stages {
		stage('LOGIN AZURE') {
			steps {
				/* colocar os jobs do estágio de Checkout aqui */
                echo "Logando no Azure com a Service Principal"
                withCredentials([azureServicePrincipal(credentialsId: 'AzureServicePrincipal',
                                        subscriptionIdVariable: 'SUBS_ID',
                                        clientIdVariable: 'CLIENT_ID',
                                        clientSecretVariable: 'CLIENT_SECRET',
                                        tenantIdVariable: 'TENANT_ID')]) {
                sh '''
                    az login --service-principal -u $CLIENT_ID -p $CLIENT_SECRET -t $TENANT_ID
                '''
                }
			}
		}
		stage('CREATE RESOURCE GROUP') {
			steps {
                sh '''
                    az group create -l westus -n $NAME
                '''
				echo "Criando o Resource Group: ${env.NAME}"
			}
		}
	}
	post {
		always {
			/* colocar as ações do bloco post aqui */
            echo "Passo 5 POS"
		}
	}
}