package com.xeniac.ethereumwalletdemo.feature_wallet.data.repository

import com.xeniac.ethereumwalletdemo.core.util.Resource
import com.xeniac.ethereumwalletdemo.core.util.UiText
import com.xeniac.ethereumwalletdemo.feature_wallet.data.local.EthWalletDao
import com.xeniac.ethereumwalletdemo.feature_wallet.domain.models.EthWalletInfo
import com.xeniac.ethereumwalletdemo.feature_wallet.domain.repository.EthWalletRepository
import org.web3j.crypto.WalletUtils
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class EthWalletRepositoryImpl @Inject constructor(
    private val ethWalletDao: EthWalletDao
) : EthWalletRepository {

    override suspend fun createOfflineEthWallet(
        password: String,
        walletFileDir: String
    ): Resource<Nothing> {
        return try {
            // Clear local database
            deleteAllEthWallets()

            val bip39Wallet = WalletUtils.generateBip39Wallet(password, File(walletFileDir))

            val walletName = bip39Wallet.filename
            val walletMnemonic = bip39Wallet.mnemonic
            val credentials = WalletUtils.loadBip39Credentials(password, walletMnemonic)
            val walletAddress = credentials.address
            val walletPrivateKey = credentials.ecKeyPair.privateKey.toString()

            // Save wallet info into local database
            val ethWalletInfo = EthWalletInfo(
                name = walletName,
                password = password,
                address = walletAddress,
                mnemonicPhrase = walletMnemonic,
                privateKey = walletPrivateKey
            )
            upsertEthWalletInfo(ethWalletInfo)

            Timber.i("Offline Eth wallet successfully created.")
            Resource.Success()
        } catch (e: Exception) {
            val errorMessage = e.message.toString()
            Timber.e("Create offline wallet failed: $errorMessage")
            Resource.Error(UiText.DynamicString(errorMessage))
        }
    }

    override suspend fun upsertEthWalletInfo(ethWalletInfo: EthWalletInfo): Resource<Nothing> {
        return try {
            ethWalletDao.upsertEthWalletInfo(ethWalletInfo.toEthWalletInfoDto())
            Timber.i("Eth wallet successfully inserted into database.")
            Resource.Success()
        } catch (e: Exception) {
            val errorMessage = e.message.toString()
            Timber.e("Upsert Ethereum wallet info failed: $errorMessage")
            Resource.Error(UiText.DynamicString(errorMessage))
        }
    }

    override suspend fun deleteAllEthWallets(): Resource<Nothing> {
        return try {
            ethWalletDao.deleteAllEthWallets()
            Timber.i("All Eth wallets successfully deleted from database.")
            Resource.Success()
        } catch (e: Exception) {
            val errorMessage = e.message.toString()
            Timber.e("Delete all Ethereum wallets failed: $errorMessage")
            Resource.Error(UiText.DynamicString(errorMessage))
        }
    }

    override suspend fun getFirstEthWalletInfo(): Resource<EthWalletInfo> {
        return try {
            val ethWalletInfoDto = ethWalletDao.getFirstEthWalletInfo()
            Resource.Success(ethWalletInfoDto?.toEthWalletInfo())
        } catch (e: Exception) {
            val errorMessage = e.message.toString()
            Timber.e("Get first Ethereum wallet info failed: $errorMessage")
            Resource.Error(UiText.DynamicString(errorMessage))
        }
    }
}