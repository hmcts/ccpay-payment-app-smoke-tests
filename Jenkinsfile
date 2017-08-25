#!groovy
@Library("Reform") _

properties([
        [$class: 'GithubProjectProperty', displayName: 'Payment API smoke tests', projectUrlStr: 'https://git.reform.hmcts.net/common-components/payment-app-smoke-tests'],
        parameters([
                string(defaultValue: 'dev', description: 'environment dev/test', name: 'environment')
        ])
])

lock('Payment API acceptance tests') {
    node {
        try {
            stage('Checkout') {
                deleteDir()
                checkout scm
            }

            stage('Run smoke tests') {
                wrap([$class: 'VaultBuildWrapper', vaultSecrets: [
                        [$class      : 'VaultSecret',
                         path        : 'secret/' + params.environment + '/cc/payment/smoke-tests/idam-email',
                         secretValues: [[$class: 'VaultSecretValue', envVar: 'IDAM_EMAIL', vaultKey: 'value']]],
                        [$class      : 'VaultSecret',
                         path        : 'secret/' + params.environment + '/cc/payment/smoke-tests/idam-password',
                         secretValues: [[$class: 'VaultSecretValue', envVar: 'IDAM_PASSWORD', vaultKey: 'value']]],
                        [$class      : 'VaultSecret',
                         path        : 'secret/' + params.environment + '/cc/payment/smoke-tests/microservice-name',
                         secretValues: [[$class: 'VaultSecretValue', envVar: 'AUTH_PROVIDER_SERVICE_CLIENT_MICROSERVICE', vaultKey: 'value']]],
                        [$class      : 'VaultSecret',
                         path        : 'secret/' + params.environment + '/cc/payment/smoke-tests/microservice-key',
                         secretValues: [[$class: 'VaultSecretValue', envVar: 'AUTH_PROVIDER_SERVICE_CLIENT_KEY', vaultKey: 'value']]]
                ]]) {
                    def rtMaven = Artifactory.newMavenBuild()
                    rtMaven.tool = 'apache-maven-3.3.9'
                    rtMaven.run pom: 'pom.xml', goals: 'clean package surefire-report:report -Dspring.profiles.active=' + params.environment

                    publishHTML([
                            allowMissing         : false,
                            alwaysLinkToLastBuild: true,
                            keepAll              : false,
                            reportDir            : 'target/site',
                            reportFiles          : 'surefire-report.html',
                            reportName           : 'Smoke Test Report'
                    ])
                }
            }
        } catch (err) {
            notifyBuildFailure channel: '#cc-payments-tech'
            throw err
        }
    }
}