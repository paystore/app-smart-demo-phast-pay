# Release Notes

## v1.0.2.0

#### refs: 173267
- incremento de versão
- comitando documentações
- Trocado texto informativo do abort por card com icone
- Ocultado botao Payment + Abort para o servico MBWAY e adicionada informacao sobre o abort apos 30 segundos
- Adicionado funcao para abortar um pagamento quando a tela de qrcode estiver aberta. - Atualizada versao do SDK

#### refs: 172786
- atualizado response do StartPaymentAPI - atualizado DTO QrCodeResponse

#### refs: 171664
- Diminuido o nivel de complexidade da função.
- Ajustado a listagem dos dados do pix
- Ajustando app demo para a feature do pix internacional

#### refs: sem_referencia
- refs  #172080 - Adicionada feature de impressao da ultima transação.

#### refs: 171376
- Permitindo o envio do currency quando nenhum valor for enviado para a aplicação.

#### refs: 171358
- Ajustado response de erro para retornar o codigo e a mensagem.

#### refs: 170945
- adicionado dados do pagamento cripto na listagem dos pagamentos
- Revert ""
- Removendo checkbox de impressão visto que não é necessário no pagamento via api
- Adicionado suporte ao serviço crypto nas requisicoes do PaymentApiService
- Adicionado serviço da crypto ao service - Configurado selector para obter os nomes dos serviços automaticamente - Ajustado dialog para inserir o qrcode - Adicionado qrcode a requisicao de resposta da criacao do pagamento
- Preparando o app demo para receber os novos dados da crypto

#### refs: 171210
- Adicionada opção do envio do providerId

## v1.0.1.0

#### refs: 165988
- Incremento de versão
- adicionar o cobExpiration na tela do GetAvailableServices

#### refs: 165881
- adicionado o service TWINT ao filtro de Service - removido alguns paddings e outros reduzidos para diminuir o tamnaho final do component
- Adicionando tela de report sem passar os dados de startDate e EndDate

#### refs: 165883
- Agrupado itens de pagamento - Agrupdado itens de consulta de pagamentos para devolucao.

#### refs: 165927
- Unindo as opções de relatorio sem filtro e com filtro na ReportMenuScreen

#### refs: 165890
- Ajustado campo de formatImage para imageFormat
- Ajustado requisicao de obtencao de qrcode. - Adicionado campos formatImage e base64 - Removido campos url e png.

#### refs: 165831
- Ajustado obtencao dos dados do qrcode para obter a logo em png.

#### refs: 165469
- Adicionando os campos de impressão na requisicao de impressao. Verificando se há algum campo de impressao ativo antes de chamar a função de impressao.
- Adicionando os campos de impressão de volta na tela de PaymentApiScreen
- Removendo opções de impressão no StartPaymentApi
- Renomeando os arquivos de features de startPaymentQrCode
- Adicionado traducao em inglês.
- Renomeando classes e fun que se relacionava com startPaymentQrCodeService para startPaymentApiService, bem como suas classes de request e response.

#### refs: 165116
- Ajustes na tela de PaymentWrCodeScreen
- Criando funcionalidade para realizar o startPaymentQrCode a partir do app demo

#### refs: 165242
- Ajuste para pegar o paymentId, appClientId e o refundId automaticamente na telde print, GetRefundById e SartRefund
- Ajustado nome da função de request para impressao.
- Habilitando o botão de impressão caso seja adicionado o refundId
- Inserido mensagem de erro caso o sdk retorne erro. - Ajustado eventos.
- Injetando as informações da aplicação usando um repository - Configurado gradlew para compilar em paralelo.
- Adicionado serviço de impressão de comprovante. - Atualizado AAR

#### refs: 165139
- adicionando registro de notificações ao abrir a tele, sem a necessidade do usuário ter que fazer isso previsamente nesse fluxo de pgamento da twint.
- ajustando merge
- Adicionando tela de loading na tela de pagamento com QRCode.  - recebendo evento de confirmação do pagamento para exibir feedback para o usuário.
- melhorando tela de qrCode, Ajustando response e request
- Criando funcionalidade para realizar o startPaymentQrCode a partir do app demo

#### refs: 165306
- Ajustado botões de ativação de input. - Ajustado titulo de requisição abortada. - Criado componente reutilizavel de Selector.
- ajsute na feature StartPayment para adicionar os dois novos parametros

#### refs: 165374
- Ajustado input currency das telas de pagamento e pagamentocore
- Ajustado input currency das telas de pagamento e pagamentocore - Movido parse do transactionStatus para o enum do componente.

#### refs: 165216
- Obtendo os dados do additional_value nas respostas das requisições de pagamento e consulta.

#### refs: 165125
- pequeno ajuste na tela de abortar pagamento
- Alguns ajuste na tela de abortar pagamento
- Adicionando fun AbortPayment app demo

#### refs: sem_referencia
- Merge branch 'feature/twint' of ssh://git.phoebus.com.br:2222/paystore-apps/app-smart-demo-phast-pay into feature/twint
- refs  #164233 - Adicionada função para obtenção do qrcode.
- Merge branch 'feature/bidirecional' into feature/twint - removendo o startManifest
- Merge branch 'feature/bidirecional' into feature/twint
- Poc - Bidirecional

#### refs: 164927
- Criando fun responsável por remover registro de notificações  - ajustando fun que não estava sendo usada showReceipt para paymentUpdate  - Ajuste na tela que faz o registro das notificações e remove o registro

#### refs: 164600
- Melhoria para evitar que o Android coloque a aplicação que deseja ouvir os eventos em freezing  - Criando serviço de foreground para indicar para o android que o serviço é importante.  - adicionando permissão necessária.

#### refs: 164394
- delegando escuta dos eventos para PhastPayIpcManager para que a escuta seja global no app e removendo da RegisterNotifyScreen  - melhoria do visual da tela RegisterNotifyScreen
- Substituindo o Gson pelo Kotlin Serialize para menor consumo de memoria e performace.  - removendo responsabilitade da view de fazer a conversão do json e deixando a cargo da viewModel  - removendo warnnings
- Substituindo o Gson pelo Kotlin Serialize para menor consumo de memoria e performace.  - removendo responsabilitade da view de fazer a conversão do json e deixando a cargo da viewModel  - removendo warnnings  - fechando o app após sair da tela home
- Substituindo o Gson pelo Kotlin Serialize para menor consumo de memoria e performace.  - usando kotlin serializer nas rotas para validar e converter os parametros
- Substituindo o Gson pelo Kotlin Serialize para menor consumo de memoria e performace.  - removendo instancia dos serviços de forma manual e deixando a cargo do Hilt gerenciar.
- Substituindo o Gson pelo Kotlin Serialize para menor consumo de memoria e performace.  - Injetando o Kotlin Serialize nos serviços
- Substituindo o Gson pelo Kotlin Serialize para menor consumo de memoria e performace.  - adicionando dependencias para o kotlin-serialization
- Substituindo o Gson pelo Kotlin Serialize para menor consumo de memoria e performace.

#### refs: 164498
- Melhoria no layout de filtro por status - Melhoria no layout dos checkbox - Alterado a cor do button nas telas de pagamento e consulta - Removido campos preview na request do PhastPayGetPaymentById

#### refs: 164317
- Adicionando novos campos previewMerchantReceipt e previewCustomerReceipt

#### refs: 164261
- Ajustado validação de exibição do dialog.
- Readicionado Dialog, caso não exista informações de resources disponiveis, mantendo a retrocompatibilidade com versões antigas do phastpay.
- Adicionado ajuste na tela de serviços para exibir todos os provedores disponiveis, informações de tips e serviços de cada provedor.

#### refs: 164281
- Adicionado tema do app demo para fica de acordo com as cores do phastpay. - Adicionado tela de splash.
- Injetado hilt em todas os view models da aplicação. - Corrigido problema da tela branca ao retornar da tela de menu. - Alterado nome do navigation para MainNavHost - Separado a tela de main da tela de home. - Ajustado verficiação de tela inicial. - Injetado hilt na mainActivity
- Adicionado bibliotecas do hilt ao projeto.

#### refs: 164216
- Atualizando o app demo para exibir uma notificação quando o evento ping for recebido
