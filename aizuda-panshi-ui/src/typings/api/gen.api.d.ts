/**
 * Namespace Api
 *
 * All backend api type
 */
declare namespace Api {
  /**
   * namespace Gen
   *
   * backend api module: "gen"
   */
  namespace Gen {
    /**
     * 数据源类型
     *
     * - mysql
     * - oralce
     * - pgsql
     */
    type DatabaseType = 'mysql' | 'oracle' | 'pgsql';

    /** 代码生成数据源 */
    type Database = Common.CommonRecord<{
      /** 别名 */
      alias: string;
      /** 数据库名 */
      dbName: string;
      /** 主机 */
      host: string;
      /** 密码 */
      password: string;
      /** 端口 */
      port: number;
      /** 类型 */
      type: DatabaseType;
      /** 用户名 */
      username: string;
    }>;

    /** database search params */
    type DatabaseSearchParams = CommonType.RecordNullable<
      Api.Common.CommonSearchParams & {
        data: CommonType.RecordNullable<Pick<Database, 'dbName' | 'alias' | 'type'>>;
      }
    >;

    /** database form params */
    type DatabaseFormParams = CommonType.RecordNullable<
      Pick<Database, 'id' | 'dbName' | 'alias' | 'type' | 'username' | 'password' | 'port' | 'host'>
    >;

    /** database list */
    type DatabaseList = Common.PaginatingQueryRecord<Database>;

    /** 代码生成模板 */
    type Template = Common.CommonRecord<{
      /** 输出文件 */
      outFile: string;
      /** 模板内容 */
      tplContent: string;
      /** 模板名称 */
      tplName: string;
    }>;

    /** template search params */
    type TemplateSearchParams = CommonType.RecordNullable<
      Api.Common.CommonSearchParams & {
        data: CommonType.RecordNullable<Pick<Template, 'tplName'>>;
      }
    >;

    /** template form params */
    type TemplateFormParams = CommonType.RecordNullable<
      Pick<Template, 'id' | 'outFile' | 'tplContent' | 'tplName' | 'remark'>
    >;

    /** template list */
    type TemplateList = Common.PaginatingQueryRecord<Template>;

    /** 代码生成 */
    type GenCode = {
      /** 数据源 ID */
      databaseId: string | null | undefined;
      /** 作者 */
      author: string;
      /** 项目模块 */
      module: string;
      /** 表名 */
      tableName: string;
      /** 模板 ID 列表 */
      templateIds: string[];
    };

    /** 代码生成预览 */
    type GenPreview = {
      /** 模板名称 */
      tplName: string;
      /** 模板内容 */
      tplContent: string;
      /** 输出文件 */
      outFile: string;
      /** 模板描述 */
      remark: string;
    };
  }
}
