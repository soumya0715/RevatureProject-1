# **ARCHITECTURE DIAGRAM (RevPassword Manager)**







+------------------+

|      **Main**        |

+------------------+

&nbsp;       |

&nbsp;       v

+------------------+

|    **Service       |**

**|  - UserService**   |

+------------------+

&nbsp;       |

&nbsp;       v

+------------------+        +--**----------------+**

**|      DAO         |        |      Util        |**

**| - UserDao        |        | - EmailUtil      |**

**| - PasswordDao    |        | - DbConnection   |**

**| - SecurityQDao   |        | - EncryptionUtil |**

**| - VerificationCDa|        | - PasswordGen**    |

+------------------+        +------------------+

&nbsp;       |

&nbsp;       v

+---------------------------------------------+

|                  **Model                      |**

**| - User                                      |**

**| - Password                                  |**

**| - VerificationCode                          |**

**| - SecurityQuestion**                          |

+---------------------------------------------+

