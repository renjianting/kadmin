package pt.tecnico.dsi.kadmin

import java.io.File

import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterEach, FlatSpec}

/**
  * $assumptions
  */
class TicketsSpec extends FlatSpec with TestUtils with BeforeAndAfterEach {
  import fullPermissionsKadmin.settings.{principal, password, realm}

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    KadminUtils.destroyTickets().value
  }

  override protected def afterEach(): Unit = {
    super.afterEach()
    KadminUtils.destroyTickets().value
  }

  val kadmin = new Kadmin(ConfigFactory.parseString(s"""
    kadmin {
      realm = "EXAMPLE.COM"
      password-authentication = false
      command = "kadmin -c /tmp/krb5cc_0"
    }"""))


  "obtainTicketGrantingTicket" should "throw IllegalArgumentException if neither password or keytab is specified" in {
    intercept[IllegalArgumentException]{
      KadminUtils.obtainTGT("", principal)
    }
  }
  it should "throw IllegalArgumentException if both password and keytab are specified" in {
    intercept[IllegalArgumentException]{
      KadminUtils.obtainTGT("", principal, password = Some(password), keytab = Some(new File(".")))
    }
  }
  it should "succeed with password" in {
    //obtain a TGT. We pass the -S flag so we can later use kadmin with the obtained ticket
    KadminUtils.obtainTGT(s"-S $principal", principal, password = Some(password)).rightValueShouldBeUnit()

    //Try to access kadmin using the credencial cache created when obtaining the TGT
    kadmin.getPrincipal(principal).rightValue.name should startWith (principal)
  }
  it should "succeed with keytab" in {
    //Create the keytab
    val principalWithKeytab = "test/admin"
    fullPermissionsKadmin.addPrincipal("", principalWithKeytab, randKey = true).rightValueShouldBeUnit()
    fullPermissionsKadmin.createKeytab("", principalWithKeytab).rightValueShouldBeUnit()

    val keytabFile = fullPermissionsKadmin.getKeytabFile(principalWithKeytab)

    //Obtain a TGT using the keytab. We pass the -S flag so we can later use kadmin with the obtained ticket.
    KadminUtils.obtainTGT(s"-S $principal", principalWithKeytab, keytab = Some(keytabFile)).rightValueShouldBeUnit()


    val kadminWithKeytab = new Kadmin(ConfigFactory.parseString(s"""
    kadmin {
      realm = "EXAMPLE.COM"
      password-authentication = false
      principal = "$principalWithKeytab"
      command = "kadmin -kt ${keytabFile.getAbsolutePath} -p $$FULL_PRINCIPAL"
    }"""))

    //Then we try to access kadmin using the keytab
    kadminWithKeytab.getPrincipal(principalWithKeytab).rightValue.name should startWith (principalWithKeytab)
  }

  "listTickets" should "succeed" in {
    //Obtain a TGT
    KadminUtils.obtainTGT(principal = principal, password = Some(password)).rightValueShouldBeUnit()

    val tickets = KadminUtils.listTickets().value
    tickets.exists(_.servicePrincipal == s"krbtgt/$realm@$realm") shouldBe true
  }

  "destroyTickets" should "succeed" in {
    //Ensure we have the ticket and it is working
    KadminUtils.obtainTGT(s"-S $principal", principal, password = Some(password)).rightValueShouldBeUnit()
    kadmin.getPrincipal(principal).rightValue.name should startWith (principal)

    KadminUtils.destroyTickets().value.shouldBe(())
    kadmin.getPrincipal(principal).leftValue shouldBe a [UnknownError]
  }
}
